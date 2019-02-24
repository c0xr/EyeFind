package com.csti.eyefind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.ULocale;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private Button Login;
    private Button register;
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isremember;
    private String s_account;
    private String s_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.remember_pass);
        Login = findViewById(R.id.login);
        register=findViewById(R.id.register);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        isremember = sharedPreferences.getBoolean("isremember", false);
        if (isremember) {
            s_account = sharedPreferences.getString("account", "");
            s_password = sharedPreferences.getString("password", "");
            account.setText(s_account);
            password.setText(s_password);
            checkBox.setChecked(true);
        }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Person user = new Person();
                //此处替换为你的用户名
                user.setUsername(account.getText().toString());
                //此处替换为你的密码
                user.setPassword(password.getText().toString());
                user.login(new SaveListener<Person>() {
                    @Override
                    public void done(Person bmobUser, BmobException e) {
                        if (e == null) {
                            Person user = BmobUser.getCurrentUser(Person.class);
                            Toast.makeText(LoginActivity.this,"欢迎回来"+user.getmName(),Toast.LENGTH_SHORT).show();

                            BmobQuery<LostItem> query = new BmobQuery<>();
                            query.addWhereEqualTo("mPerson", user);
                            query.findObjects(new FindListener<LostItem>() {
                                @Override
                                public void done(List<LostItem> object,BmobException e) {
                                    if(e==null){
                                        //Toast.makeText(LoginActivity.this,"成功"+object.size(),Toast.LENGTH_SHORT).show();
                                        String[] Lostthing=new String[object.size()];
                                        for(int i=0;i<object.size();i++){
                                            Lostthing[i]=object.get(i).getLabel();
                                        }
                                        BmobInstallationManager.getInstance().subscribe(Arrays.asList(Lostthing), new InstallationListener<BmobInstallation>() {
                                            @Override
                                            public void done(BmobInstallation bmobInstallation, BmobException e) {
                                                if (e == null) {
                                                    //Toast.makeText(LoginActivity.this, "批量订阅成功", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }else{
                                        Toast.makeText(LoginActivity.this,"失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                            editor=sharedPreferences.edit();
                            if (checkBox.isChecked()) {
                                editor.putString("account",user.getUsername());/////////////////////////////////
                                editor.putString("password",user.getmPassword());
                                editor.putBoolean("isremember",true);
                            }else {
                                editor.clear();
                            }
                            editor.apply();
                            String objectId = user.getObjectId();
                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                            intent.putExtra("objectId", objectId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,"失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
