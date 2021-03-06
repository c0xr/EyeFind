package com.csti.eyefind.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class NotificationActivity extends AppCompatActivity {
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
    private Bitmap bitmap[] = new Bitmap[2];
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0){
                mImageA.setImageBitmap(bitmap[0]);
                mImageB.setImageBitmap(bitmap[1]);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mImageA=findViewById(R.id.bitmap_a);
        mImageB=findViewById(R.id.bitmap_b);
        mName=findViewById(R.id.name_text_view);
        mPlace=findViewById(R.id.place_text_view);
        mFounder=findViewById(R.id.founder_text_view);
        mDate=findViewById(R.id.date_text_view);
        mTel=findViewById(R.id.tel_text_view);
        mQQ=findViewById(R.id.qq_text_view);
        mWeChat=findViewById(R.id.wechat_text_view);
        Intent intent=getIntent();
        String objectId=intent.getStringExtra("objectid");
        Log.d("活动ID","sssss"+objectId);
        BmobQuery<LostItem> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<LostItem>() {
            @Override
            public void done(LostItem lostItem, BmobException e) {
                mLostItem=lostItem;
                mName.setText(lostItem.getName());
                mPlace.setText(lostItem.getPickedPlace());
                mFounder.setText(lostItem.getFounder());
                mDate.setText(lostItem.getPickedDate());
                mTel.setText(lostItem.getTel());
                mQQ.setText(lostItem.getQQ());
                mWeChat.setText(lostItem.getWeChat());
                setBitmap(lostItem.getImageA().getUrl(),lostItem.getImageB().getFileUrl());

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
        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.isLogin()){
                    if (mLostItem.ismIsConfirmed()) {
                        I_pick_thing.showDialog("该物品目前已经有人认领，请联系拾取人确认，防止冒领", null, NotificationActivity.this);
                    } else {
                        LostItem mlostItem1 = new LostItem();
                        mlostItem1.setConfirmed(true);
                        mlostItem1.update(mLostItem.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                                    builder.setTitle("EyeFind")
                                            .setMessage("已确认该物品,等待对方确认")
                                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    Toast.makeText(NotificationActivity.this, "确认失败,请稍后重试", Toast.LENGTH_SHORT).show();
                                    Log.d("mytag", e + "");
                                }
                            }
                        });
                    }
                }else{
                    I_pick_thing.showDialog("请登录！",null,NotificationActivity.this);
                }
            }
        });
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
    private void setBitmap(final String s1 , final String s2){
        new Thread(){
            @Override
            public void run() {
                bitmap[0] = getPicture(s1);
                bitmap[1] = getPicture(s2);
                Message message = new Message();
                message.what = 0x0;
                handler.sendMessage(message);
            }
        }.start();
    }


    public Bitmap getPicture(String path){
        Bitmap bm = null;
        try{
            URL url = new URL(path);
            URLConnection connection=url.openConnection();
            connection.connect();
            InputStream inputStream=connection.getInputStream();
            bm= BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
}
