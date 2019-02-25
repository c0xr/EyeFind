package com.csti.eyefind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

    private TextView mTextMessage;
    private String mUserAccount;//用户账号//学号
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
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

                    startActivity(new Intent(MainActivity.this, MyProperty.class));
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

        Bmob.initialize(this, "a744c289f17c26d9df110a2fa115feaf");

        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    //Toast.makeText(MainActivity.this, "该设备已经在设备表注册", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 启动推送服务
        BmobPush.startWork(this);


        final Button I_pick_thing = findViewById(R.id.I_pick_thing);
        I_pick_thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                String objectId = sharedPreferences.getString("objectId", "");
                mUserAccount = sharedPreferences.getString("account", "");
                if (!(objectId.equals(" "))) {
                    Intent intent = new Intent(MainActivity.this, I_pick_thing.class);
                    startActivity(intent);
                }else {
                    com.csti.eyefind.activities.I_pick_thing.showDialog("请登录!", null, MainActivity.this);
                }

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //test
        findViewById(R.id.I_will_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OverviewActivity.newIntent(MainActivity.this));
            }
        });

    }

}
