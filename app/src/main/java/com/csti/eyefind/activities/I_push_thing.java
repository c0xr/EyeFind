package com.csti.eyefind.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.util.Arrays;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class I_push_thing extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner indicator_push_thing;
    private ArrayAdapter<CharSequence> adapter;
    private EditText push_lostitem;
    private ImageButton push;
    private String mName;//名称
    private String mLabel;//标签(类别)
    private PushLostItem pushLostItem=new PushLostItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_push_thing);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("添加挂失信息");
        indicator_push_thing = findViewById(R.id.indicator_push_thing);
        push_lostitem=findViewById(R.id.push_lostitem);
        push = findViewById(R.id.push);
        initSpinner();

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName=push_lostitem.getText().toString();
                if(!(mName.equals(""))){
                    savePost();
                }else{
                    I_pick_thing.showDialog("请输入物品名称！",null,I_push_thing.this);
                }
            }
        });

    }
    private void savePost() {
        if (BmobUser.isLogin()) {
            //添加一对一关联，用户关联帖子
            pushLostItem.setmPerson(BmobUser.getCurrentUser(Person.class));
            pushLostItem.setmName(mName);
            pushLostItem.setmLabel(mLabel);
            pushLostItem.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Toast.makeText(I_push_thing.this, "添加数据成功", Toast.LENGTH_SHORT).show();
                        BmobInstallationManager.getInstance().subscribe(Arrays.asList(mLabel), new InstallationListener<BmobInstallation>() {
                            @Override
                            public void done(BmobInstallation bmobInstallation, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(I_push_thing.this,"成功",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(I_push_thing.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        finish();
                    } else {
                        Toast.makeText(I_push_thing.this, "添加数据失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(I_push_thing.this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }
    //初始化Spinner
    private void initSpinner() {
        adapter = ArrayAdapter.createFromResource(I_push_thing.this,
                R.array.indicator_strings, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indicator_push_thing.setAdapter(adapter);
        indicator_push_thing.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mLabel = (String) indicator_push_thing.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
