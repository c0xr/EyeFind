package com.csti.eyefind.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csti.eyefind.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class MyLostDetailActivity extends AppCompatActivity {
    private final static String EXTRA_ID="id";
    private final static String EXTRA_ADAPTER_TYPE="adapter type";

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
        setContentView(R.layout.activity_my_lost_detail);
        getProperty();
    }

    private void getProperty(){
        Intent intent = getIntent();
        Bundle b=intent.getExtras();

        setBitmap(intent.getStringExtra("bitmapA"),intent.getStringExtra("bitmapB"));

        mImageA = findViewById(R.id.bitmap_a);
        mImageB = findViewById(R.id.bitmap_b);
        mName = findViewById(R.id.name_text_view);
        mPlace =findViewById(R.id.place_text_view);
        mFounder = findViewById(R.id.founder_text_view);
        mDate = findViewById(R.id.date_text_view);
        mTel = findViewById(R.id.tel_text_view);
        mQQ = findViewById(R.id.qq_text_view);
        mWeChat = findViewById(R.id.wechat_text_view);

//        ViewGroup.LayoutParams params=mImageB.getLayoutParams();
//        float ratio=(float) MeasureUtil.dp2px(300,this)/mLostItem.getBitmapB().getHeight();
//        params.width=(int)(mLostItem.getBitmapB().getWidth()*ratio);
//        mImageA.setLayoutParams(params);

        mName.setText(intent.getStringExtra("mName"));
        mPlace.setText(intent.getStringExtra("mPlace"));
        mFounder.setText(intent.getStringExtra("mFounder"));
        mDate.setText(intent.getStringExtra("mDate"));
        mTel.setText(intent.getStringExtra("mTel"));
        mQQ.setText(intent.getStringExtra("mQQ"));
        mWeChat.setText(intent.getStringExtra("mWeChat"));
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
