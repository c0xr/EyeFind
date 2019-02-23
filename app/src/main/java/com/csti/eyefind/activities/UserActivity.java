package com.csti.eyefind.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class UserActivity extends AppCompatActivity {

    private TextView user_name, user_sex, user_college, user_major, user_id;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bmob.initialize(this, "a744c289f17c26d9df110a2fa115feaf");
        user_college = findViewById(R.id.user_college);
        user_id = findViewById(R.id.user_id);
        user_major = findViewById(R.id.user_major);
        user_name = findViewById(R.id.user_name);
        user_sex = findViewById(R.id.user_sex);
        exit = findViewById(R.id.exit);
        Intent intent = getIntent();
        final String objectId = intent.getStringExtra("objectId");
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("objectId",objectId);
        editor.apply();
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        bmobQuery.getObject(objectId, new QueryListener<Person>() {
            @Override
            public void done(Person object, BmobException e) {
                if (e == null) {
                    user_college.setText(object.getmCollege());
                    user_id.setText(object.getUsername() + "");/////////////////////
                    user_major.setText(object.getmMajor());
                    user_name.setText(object.getmName());//////////////////////////
                    user_sex.setText(object.getmSex());
                } else {
                    //toast("查询失败：" + e.getMessage());
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dia = new AlertDialog.Builder(UserActivity.this);
                dia.setTitle("是否要退出登录");
                dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                        editor.putString("objectId"," ");
                        editor.apply();
                        finish();
                    }
                });
                dia.setNegativeButton("取消",null);
                dia.show();

            }
        });

    }
}
