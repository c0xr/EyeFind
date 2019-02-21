package com.csti.eyefind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(MainActivity.this, MyProperty.class));
//                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//                    editor.putString("objectId", "3533107f3e");
//                    editor.apply();
//                    Person p2 = new Person();
//                    p2.setmName("黄元昊");
//                    p2.setmSex("男");
//                    p2.setmCollege("软件学院");
//                    p2.setmMajor("软件工程");
//                    p2.setmId(20175999);
//                    p2.setmPassword("123456");
//                    p2.save(new SaveListener<String>() {
//                        @Override
//                        public void done(String objectId,BmobException e) {
//                            if(e==null){
//                                Toast.makeText(MainActivity.this,"添加数据成功，返回objectId为："+objectId,Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(MainActivity.this,"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                    return true;
                case R.id.navigation_notifications:
                    SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
                    final String objectId = preferences.getString("objectId", "0000");
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


        Button I_pick_thing = findViewById(R.id.I_pick_thing);
        I_pick_thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, I_pick_thing.class);
                startActivity(intent);

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
