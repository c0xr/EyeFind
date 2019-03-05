package com.csti.eyefind.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.BatchUpdates;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class DetailActivity extends AppCompatActivity {
    private final static String EXTRA_ID="id";
    private final static String EXTRA_ADAPTER_TYPE="adapter type";
    private final static String EXTRA_OBJECT_ID="object id";
    private final static String EXTRA_FROM_LIST="from list";
    private LostItem mLostItem;
    private ImageView mImageA;
    private ImageView mImageB;
    private TextView mName;
    private TextView mPlace;
    private TextView mFounder;
    private TextView mDate;
    private TextView mTel;
    private TextView mQQ;
    private TextView mWeChat;
    private ImageLoaderDetail mImageLoaderDetail;
    private NetworkHandler mNetworkHandler;
    private TextView mOption;
    private String[] mOptionStrings;
    private String mOptionPrefix;
    private LinearLayout mOptionLayout;

    public static Intent newIntent(Context packageContext,String adapterType,UUID id,boolean fromList){
        Intent intent=new Intent(packageContext,DetailActivity.class);
        intent.putExtra(EXTRA_ADAPTER_TYPE,adapterType);
        intent.putExtra(EXTRA_ID,id);
        intent.putExtra(EXTRA_FROM_LIST,fromList);
        return intent;
    }

    public static Intent newIntent(Context packageContext,String objectId,boolean fromList){
        Intent intent=new Intent(packageContext,DetailActivity.class);
        intent.putExtra(EXTRA_OBJECT_ID,objectId);
        intent.putExtra(EXTRA_FROM_LIST,fromList);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String adapterType=getIntent().getStringExtra(EXTRA_ADAPTER_TYPE);
        UUID id=(UUID) getIntent().getSerializableExtra(EXTRA_ID);
        String objectId=getIntent().getStringExtra(EXTRA_OBJECT_ID);
        boolean fromList=getIntent().getBooleanExtra(EXTRA_FROM_LIST,false);

        if(fromList) {
            mLostItem = LostItemLab.getInstance(this).getLostItem(adapterType, id);
        }else {
            mLostItem=new LostItem();
            mLostItem.setObjectId(objectId);
        }

        mImageA=findViewById(R.id.bitmap_a);
        mImageB=findViewById(R.id.bitmap_b);
        mName=findViewById(R.id.name_text_view);
        mPlace=findViewById(R.id.place_text_view);
        mFounder=findViewById(R.id.founder_text_view);
        mDate=findViewById(R.id.date_text_view);
        mTel=findViewById(R.id.tel_text_view);
        mQQ=findViewById(R.id.qq_text_view);
        mWeChat=findViewById(R.id.wechat_text_view);
        mOption=findViewById(R.id.option);
        mOptionLayout=findViewById(R.id.option_layout);

        mOptionStrings=getResources().getStringArray(R.array.option_strings);
        mOptionPrefix=getResources().getString(R.string.detail_option_prefix);

        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.isLogin()){
                    if (mLostItem.ismIsConfirmed()) {
                        I_pick_thing.showDialog("该物品目前已经有人认领，请联系拾取人确认，防止冒领", null, DetailActivity.this);
                    } else {
                        LostItem mlostItem1 = new LostItem();
                        mlostItem1.setConfirmed(true);
                        mlostItem1.update(mLostItem.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                                    builder.setTitle("EyeFind")
                                            .setMessage("已确认该物品,等待对方确认")
                                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    Toast.makeText(DetailActivity.this, "确认失败,请稍后重试", Toast.LENGTH_SHORT).show();
                                    Log.d("mytag", e + "");
                                }
                            }
                        });
                    }
            }else{
                    I_pick_thing.showDialog("请登录！",null,DetailActivity.this);
                }
            }
        });
        findViewById(R.id.call_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
                }else{
                    callPhone();
                }
            }
        });

        mNetworkHandler=new NetworkHandler();
        mImageLoaderDetail=new ImageLoaderDetail(mNetworkHandler,mLostItem);
        mImageLoaderDetail.load();
    }

    private class NetworkHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            updateUI();
        }
    }

    private void updateUI(){
        ViewGroup.LayoutParams paramsA=mImageA.getLayoutParams();
        float ratioA=(float) MeasureUtil.dp2px(400,this)/mLostItem.getBitmapA().getHeight();
        paramsA.width=(int)(mLostItem.getBitmapA().getWidth()*ratioA);

        mImageA.setLayoutParams(paramsA);
        mImageA.setImageBitmap(mLostItem.getBitmapA());

        ViewGroup.LayoutParams paramsB=mImageB.getLayoutParams();
        float ratioB=(float) MeasureUtil.dp2px(400,this)/mLostItem.getBitmapB().getHeight();
        paramsB.width=(int)(mLostItem.getBitmapB().getWidth()*ratioB);

        mImageB.setLayoutParams(paramsB);
        mImageB.setImageBitmap(mLostItem.getBitmapB());

        mName.setText(mLostItem.getName());
        mPlace.setText(mLostItem.getPickedPlace());
        mFounder.setText(mLostItem.getFounder());
        mDate.setText(mLostItem.getPickedDate());
        mTel.setText(mLostItem.getTel());
        mQQ.setText(mLostItem.getQQ());
        mWeChat.setText(mLostItem.getWeChat());
        int op=mLostItem.getOption();
        if(op!=mOptionStrings.length-1){
            mOption.setText(mOptionPrefix+mOptionStrings[op]);
            mOptionLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        callPhone();
    }

    private void callPhone(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+mLostItem.getTel()));
        startActivity(intent);
    }
}
