package com.csti.eyefind.activities;

import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class I_pick_thing extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int sTAKE_PHOTO1 = 1;
    public static final int sTAKE_PHOTO2 = 2;
    private Uri imageUri;
    private ImageView addphoto1;
    private ImageView addphoto2;
    private Button find_time;
    private Button submit_information;
    private Spinner indicator_thing;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_pick_thing);

        addphoto1 = findViewById(R.id.addphoto1);
        addphoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto("addphoto1.jpg",sTAKE_PHOTO1);
            }
        });


        addphoto2 = findViewById(R.id.addphoto2);
        addphoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto("addphoto2.jpg",sTAKE_PHOTO2);
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

        submit_information=findViewById(R.id.submit_information);
        submit_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
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
        String item = (String) indicator_thing.getItemAtPosition(position);
        //在这得到选中信息
        Toast.makeText(I_pick_thing.this, "您选中了" + item + "选项", Toast.LENGTH_SHORT).show();
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
                        addphoto2.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    private void takePhoto(String photoName,int flag_TAKE_PHOTO){
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
    }
}
