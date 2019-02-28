package com.csti.eyefind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private Fragment myFragment = new MainMyPropertyFragment();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            myFragment.show
            FragmentTransaction myTransaction = getSupportFragmentManager().beginTransaction();
            myTransaction.replace(R.id.main_fragment, myFragment).commitAllowingStateLoss();
        }
    };

    private TextView mTextMessage;
    private String mUserAccount;//用户账号//学号
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    myFragment.setVi
                    Fragment fragment = new MainPrimeFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, fragment).commitAllowingStateLoss();
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    /*一对多表查询
                    Person user = BmobUser.getCurrentUser(Person.class);
                    BmobQuery<LostItem> query = new BmobQuery<>();
                    query.addWhereEqualTo("mPerson", user);
                    query.findObjects(new FindListener<LostItem>() {

                        @Override
                        public void done(List<LostItem> object,BmobException e) {
                            if(e==null){
                                Toast.makeText(MainActivity.this,"成功"+object.size(),Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(MainActivity.this,"失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
*/
//                    SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
//                    String objectGetId = sharedPreferences.getString("objectId", "");
//                    mUserAccount = sharedPreferences.getString("account", "");
//                    if (!(objectGetId.equals(" "))) {
                    if(BmobUser.isLogin()) {
                        runnable.run();
//                        startActivity(new Intent(MainActivity.this, MyProperty.class));
                    }else {
                        com.csti.eyefind.activities.I_pick_thing.showDialog("请登录!", null, MainActivity.this);
                    }
                    return true;
                case R.id.navigation_notifications:
                    SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
                    final String objectId = preferences.getString("objectId", "000");
                    //查找Person表里面id为3533107f3e的数据
                    BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
                    bmobQuery.getObject(objectId, new QueryListener<Person>() {
                        @Override
                        public void done(Person object, BmobException e) {
                            if (e == null) {
                                //进用户页面
                                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                                intent.putExtra("objectId", objectId);
                                startActivity(intent);
                                //Toast.makeText(MainActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                            } else {
                                //进登录页面
                                //Toast.makeText(MainActivity.this,"查询失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
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


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Fragment fragment = new MainPrimeFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment).commitAllowingStateLoss();

    }

}
