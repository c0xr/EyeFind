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

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
                BmobQuery<Person> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("mId", Integer.parseInt(account.getText().toString()));
                categoryBmobQuery.findObjects(new FindListener<Person>() {
                    @Override
                    public void done(List<Person> object, BmobException e) {
                        if (e == null) {
                            //Toast.makeText(LoginActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                            String input_password = password.getText().toString();
                            String real_password = object.get(0).getmPassword() + "";
                            if (input_password.equals(real_password)) {
                                editor=sharedPreferences.edit();
                                if (checkBox.isChecked()) {
                                    editor.putString("account",object.get(0).getmId()+"");
                                    editor.putString("password",real_password);
                                    editor.putBoolean("isremember",true);
                                }else {
                                    editor.clear();
                                }
                                editor.apply();
                                String objectId = object.get(0).getObjectId();
                                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                                intent.putExtra("objectId", objectId);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();

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
