package com.csti.eyefind.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class I_pick_thing extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int sTAKE_PHOTO1 = 1;
    public static final int sTAKE_PHOTO2 = 2;
    private File photo1_File, photo2_File;
    private Uri imageUri;
    private ImageView addphoto1;
    private ImageView addphoto2;
    private EditText pick_thing_name,pick_thing_place,pick_thing_people,pick_thing_tel,pick_thing_QQ,pick_thing_VX;
    private RadioGroup pick_thing_option;
    private RadioButton radioButton;
    private Button find_time;
    private Button submit_information;
    private Spinner indicator_thing;
    private ArrayAdapter<CharSequence> adapter;
    private Bitmap photo1, photo2;//捡到东西的两张图
    private String mName;//名称
    private String mLabel;//标签(类别)
    private String mFounder;//拾取人
    private String mTel;//电话
    private String mQQ;//QQ
    private String mWeChat;//微信
    private String mPickedPlace;//拾取地点
    private String mPickedDate;//拾取日期
    private int mOption;//转交他人（暂存门卫）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_pick_thing);
        Bmob.initialize(this, "a744c289f17c26d9df110a2fa115feaf");

        final LostItem lostItem = new LostItem();
        pick_thing_name=findViewById(R.id.pick_thing_name);
        pick_thing_people=findViewById(R.id.pick_thing_people);
        pick_thing_place=findViewById(R.id.pick_thing_place);
        pick_thing_tel=findViewById(R.id.pick_thing_tel);
        pick_thing_QQ=findViewById(R.id.pick_thing_QQ);
        pick_thing_VX=findViewById(R.id.pick_thing_VX);
        addphoto1 = findViewById(R.id.addphoto1);
        pick_thing_option=findViewById(R.id.pick_thing_option);
        pick_thing_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        addphoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo1_File = takePhoto(lostItem.getId() + "addphoto1.jpg", sTAKE_PHOTO1);//拍照并获取图片真实路径
            }
        });


        addphoto2 = findViewById(R.id.addphoto2);
        addphoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo2_File = takePhoto(lostItem.getId() + "addphoto2.jpg", sTAKE_PHOTO2);//拍照并获取图片真实路径
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setSubtitle("添加拾取物信息");

        find_time = findViewById(R.id.find_time);
        find_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DatapickerFragment());
            }
        });

        indicator_thing = findViewById(R.id.indicator_thing);
        adapter = ArrayAdapter.createFromResource(I_pick_thing.this,
                R.array.indicator_strings, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indicator_thing.setAdapter(adapter);
        //注册监听器
        indicator_thing.setOnItemSelectedListener(this);

        submit_information = findViewById(R.id.submit_information);
        submit_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                String user_Id = sharedPreferences.getString("account", "");
                //根据账号是否在本地存在判断是否登录
                if (!(user_Id.equals(""))) {

                    Toast.makeText(I_pick_thing.this, user_Id, Toast.LENGTH_SHORT).show();


                } else {
                    AlertDialog.Builder dia = new AlertDialog.Builder(I_pick_thing.this);
                    dia.setTitle("请登录！");
                    dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dia.show();

                }

            }
        });


    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.datePicker_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public Button getFind_time() {
        return find_time;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mLabel = (String) indicator_thing.getItemAtPosition(position);
        //在这得到选中信息
        //Toast.makeText(I_pick_thing.this, "您选中了" + item + "选项", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case sTAKE_PHOTO1:
                if (resultCode == RESULT_OK) {
                    //如果成功
                    try {


                        //调用BitmapFactory的decodeStream（）方法将output_image.jpg解析成Bitmap对象，然后把它设置到ImageView中显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        photo1 = bitmap;
                        addphoto1.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case sTAKE_PHOTO2:
                if (resultCode == RESULT_OK) {
                    //如果成功
                    try {
                        //调用BitmapFactory的decodeStream（）方法将output_image.jpg解析成Bitmap对象，然后把它设置到ImageView中显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        photo2 = bitmap;
                        addphoto2.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private File takePhoto(String photoName, int flag_TAKE_PHOTO) {//////////////////
        //创建File对象，用于储存拍照后的图片
        File outputImage = new File(getExternalCacheDir(), photoName);
        try {
            //进行判断，如果outputImage文件已经存在，则把它删除，不存在，则创建这样一个文件
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //判断系统版本是否低于Android7.0
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(I_pick_thing.this, "com.csti.eyefind.fileprovider", outputImage);//第一个参数是一个context对象，第二个参数是任意唯一的字符串，第三个参数是刚刚创建的File对象
            //Log.d("MainActivity", outputImage.toString() + "手机系统版本高于Android7.0");
        } else {
            //调用fromFile()方法将File对象转换成Uri对象，这个Uri对象标识着output_image.jpg这张图片的本地真实路径
            //Log.d("MainActivity", outputImage.toString() + "手机系统版本低于Android7.0");
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, flag_TAKE_PHOTO);//TAKE_PHOTO是自己定义的静态常量，为1
        return outputImage;

    }

    private static void push_Photo(File photo_file) {
        final BmobFile file = new BmobFile(photo_file);
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    PhotoTest photoTest = new PhotoTest();
                    photoTest.setImg(file);
                    photoTest.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //Toast.makeText(I_pick_thing.this, "添加数据成功，返回objectId为：", Toast.LENGTH_SHORT).show();
                                Log.d("I_pick_thing", "添加数据成功");
                            } else {
                                //Toast.makeText(I_pick_thing.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("I_pick_thing", "添加数据失败" + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

}
