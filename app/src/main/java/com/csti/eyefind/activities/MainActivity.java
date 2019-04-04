package com.csti.eyefind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import android.widget.Toast;
import android.widget.VideoView;

import com.csti.eyefind.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction transaction ;
    private Fragment primeFragment ;
    private Fragment myFragment ;
    private Fragment homePageFragment;
    private Fragment LogInFragment;
    private ActionBar actionBar;

    private String objectId;//用户账号//学号

    private Person person;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    actionBar.setElevation(0);
                    transaction.hide(myFragment).hide(homePageFragment).hide(LogInFragment).show(primeFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    if(BmobUser.isLogin()) {
                        actionBar.setElevation(0);
                        transaction.hide(primeFragment).hide(homePageFragment).hide(LogInFragment).show(myFragment).commit();
                    }else {
                        Toasty.error(MainActivity.this,"请先登录",Toasty.LENGTH_SHORT).show();
                        Message message = new Message();
                        message.what = 0x0;
                        handler.sendMessage(message);
                    }
                    return true;
                case R.id.navigation_notifications:
                    //查找Person表里面id为3533107f3e的数据
//                    BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
//                    bmobQuery.getObject(objectId, new QueryListener<Person>() {
//                        @Override
//                        public void done(Person object, BmobException e) {
//                    Log.d("123123", 1+objectId+1);
                    if ( !objectId.equals("000") ) {
                        actionBar.setElevation(12);
                        //进用户页面
                        transaction.hide(myFragment).hide(primeFragment).hide(LogInFragment).show(homePageFragment).commit();
//                                actionBar.setSubtitle("用户中心");
                    } else {
                        //进登录页面
                        actionBar.setElevation(12);
                        transaction.hide(myFragment).hide(primeFragment).hide(homePageFragment).show(LogInFragment).commit();
//                                actionBar.setSubtitle("请登录");
                    }
//                        }
//                    });
                    return true;
            }
            return false;
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0){
                navigation.setSelectedItemId(R.id.navigation_notifications);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(MainActivity.this, "a744c289f17c26d9df110a2fa115feaf");
        BmobQuery<control> bmobQuery = new BmobQuery<control>();
        bmobQuery.getObject("8ca87d03e4", new QueryListener<control>() {
            @Override
            public void done(control object,BmobException e) {
                if(e==null){
                    //toast("查询成功");
                }else{
                    //toast("查询失败：" + e.getMessage());
                    I_pick_thing.showDialog("版本换了，开发者删库跑路了，告辞！", null, MainActivity.this);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            finish();
                        }
                    }.start();
                }
            }
        });
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        objectId = preferences.getString("objectId", "000");

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        actionBar = getSupportActionBar();
        actionBar.setElevation(0);

        replace();

    }

    private void replace() {
        transaction = getSupportFragmentManager().beginTransaction();
        primeFragment = new MainOverviewFragment();
        myFragment = new MainMyPropertyFragment();
        homePageFragment = new MyHomePageFragment();
        LogInFragment = new MyLogInFragment();
        transaction.add(R.id.main_fragment,primeFragment);
        transaction.add(R.id.main_fragment,myFragment);
        transaction.add(R.id.main_fragment,homePageFragment);
        transaction.add(R.id.main_fragment,LogInFragment);
        transaction.hide(myFragment).hide(homePageFragment).hide(LogInFragment).show(primeFragment).commit();
    }

    public String getObjectId(){
        return objectId;
    }

    public void setObjectId(String newObjectId){
        objectId = newObjectId;
    }

    public void changeToPrimeFragment(){
        ((MyHomePageFragment)homePageFragment).saveIfo(objectId);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    public void changeToHomePageFragment(){
        ((MyHomePageFragment) homePageFragment).changeUserViewIfo(getPerson(), objectId);
        navigation.setSelectedItemId(R.id.navigation_notifications);
    }

    public void changeToLogInFragment(){
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        objectId = preferences.getString("objectId", "000");
        navigation.setSelectedItemId(R.id.navigation_notifications);
    }

    public void setPerson(Person newPerson){
        person = newPerson;
    }

    public Person getPerson(){
        return person;
    }

}
