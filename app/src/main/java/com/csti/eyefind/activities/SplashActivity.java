package com.csti.eyefind.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csti.eyefind.R;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_TIME_LENGH = 2000;//设置闪屏时间三秒
    private Handler jumpHandler;//延迟执行，用于跳转
    private int SCREEN_WIDTH;//屏幕长宽
    private int SCREEN_HEIGHT;
    private int SHOW_SIZE_WIDTH = 0;
    private int SHOW_SIZE_HEIGHT = 0;

    private ImageView imageView;
    private TextView tv;
    private float i = 0,i2 = 0;//透明度
    private LinearLayout linearLayout;

    private Handler sizeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 000){
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(SHOW_SIZE_WIDTH,SHOW_SIZE_HEIGHT));
            }
        }
    };
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0){
                tv.setAlpha(i);
                tv.setTextSize(i2);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        imageView = (ImageView) findViewById(R.id.open_screen_image);
        tv = (TextView) findViewById(R.id.splash_tv);
//        linearLayout = (LinearLayout) findViewById(R.id.animation_screen);
//
//        SCREEN_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
//        SCREEN_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
//
//        openAnimation();
        jumpNextActivity();
        tv.setAlpha(0);
        tv.setTextSize(0);

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startAnimationA();
            }
        });

    }

    private void openAnimation(){
        new Thread(){
            @Override
            public void run() {
                while (SHOW_SIZE_WIDTH <= SCREEN_WIDTH || SHOW_SIZE_HEIGHT <= SCREEN_HEIGHT){
                    try {
                        sleep(5);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (SHOW_SIZE_WIDTH <= SCREEN_WIDTH){
                        SHOW_SIZE_WIDTH += 5;
                    }
                    if (SHOW_SIZE_HEIGHT <= SCREEN_HEIGHT){
                        SHOW_SIZE_HEIGHT += 10;
                    }
                    Message msg = new Message();
                    msg.what = 000;
                    sizeHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void jumpNextActivity(){

        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        sleep(25);
                        Message message = new Message();
                        message.what = 0x0;
                        i = (float) (i + 0.01);
                        if (i2<=50){
                            i2 = i2 + 1;
                        }
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        jumpHandler = new Handler();
        //延迟 SPLASH_DISPLAY_TIME_LENGH 时间后执行
        jumpHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        },SPLASH_DISPLAY_TIME_LENGH );

    }

    private void startAnimationA(){
        int endX=imageView.getLeft();
        int startX=endX+800;
        int endY=imageView.getTop();
        int midY=endY-650;
        imageView.setVisibility(View.VISIBLE);
        ObjectAnimator animatorX = ObjectAnimator
                .ofFloat(imageView, "x", startX, endX)
                .setDuration(1000);

        ObjectAnimator animatorYA = ObjectAnimator
                .ofFloat(imageView, "y", endY, midY)
                .setDuration(700);
        animatorYA.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator animatorYB = ObjectAnimator
                .ofFloat(imageView, "y", midY, endY)
                .setDuration(700);
        animatorYB.setInterpolator(new BounceInterpolator());
        animatorYB.setStartDelay(400);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX,animatorYA,animatorYB);
        animatorSet.start();
    }

}
