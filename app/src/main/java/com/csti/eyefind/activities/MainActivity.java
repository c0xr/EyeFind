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

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction transaction ;
    private Fragment primeFragment ;
    private Fragment myFragment ;
    private Fragment homePageFragment;
    private Fragment LogInFragment;

    private TextView mTextMessage;
    private String objectId;//用户账号//学号

//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0x0){
//                transaction.hide(myFragment).hide(primeFragment).hide(homePageFragment).show(LogInFragment).commit();
//            }
//        }
//    };

    private Person person;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.hide(myFragment).hide(homePageFragment).hide(LogInFragment).show(primeFragment).commit();
//                    replace(new MainPrimeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    if(BmobUser.isLogin()) {
                        transaction.hide(primeFragment).hide(homePageFragment).hide(LogInFragment).show(myFragment).commit();
                    }else {
                        com.csti.eyefind.activities.I_pick_thing.showDialog("请登录!", null, MainActivity.this);
                    }
                    return true;
                case R.id.navigation_notifications:
                    //查找Person表里面id为3533107f3e的数据
                    BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
                    bmobQuery.getObject(objectId, new QueryListener<Person>() {
                        @Override
                        public void done(Person object, BmobException e) {
                            if (e == null) {
                                //进用户页面
                                transaction.hide(myFragment).hide(primeFragment).hide(LogInFragment).show(homePageFragment).commit();
//                                Intent intent = new Intent(MainActivity.this, UserActivity.class);
//                                intent.putExtra("objectId", objectId);
//                                startActivity(intent);
                                //Toast.makeText(MainActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                            } else {
                                //进登录页面
                                transaction.hide(myFragment).hide(primeFragment).hide(homePageFragment).show(LogInFragment).commit();
                                //Toast.makeText(MainActivity.this,"查询失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                                startActivity(intent);
                            }
                        }
                    });
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        objectId = preferences.getString("objectId", "000");

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        replace();

    }

    private void replace() {
        transaction = getSupportFragmentManager().beginTransaction();
        primeFragment = new MainPrimeFragment();
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
