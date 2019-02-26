package com.csti.eyefind.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.I;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class I_pick_thing extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int sTAKE_PHOTO1 = 1;
    public static final int sTAKE_PHOTO2 = 2;
    private File photo1_File = null, photo2_File = null, minPhoto_File;
    private int photo1_Flag = 0, photo2_Flag = 0, minphoto_Flag = 0;
    private Uri imageUri, minimageUri;
    private ImageView addphoto1;
    private ImageView addphoto2;
    private EditText pick_thing_name, pick_thing_place, pick_thing_people, pick_thing_tel, pick_thing_QQ, pick_thing_VX;
    private RadioGroup pick_thing_option;
    private RadioButton radioButton;
    private Button find_time;
    private Button submit_information;
    private Spinner indicator_thing;
    private ArrayAdapter<CharSequence> adapter;
    private String mName;//名称
    private String mLabel;//标签(类别)
    private String mFounder;//拾取人
    private String mTel;//电话
    private String mQQ;//QQ
    private String mWeChat;//微信
    private String mPickedPlace;//拾取地点
    private String mPickedDate;//拾取日期
    private String mUserAccount;//用户账号//学号
    private int mOption = 0;//转交他人（暂存门卫）
    final LostItem lostItem = new LostItem();
    private String ID = null;//用于记录失物的objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_pick_thing);

        Bmob.initialize(this, "a744c289f17c26d9df110a2fa115feaf");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("添加拾取物信息");


        pick_thing_name = findViewById(R.id.pick_thing_name);
        pick_thing_people = findViewById(R.id.pick_thing_people);
        pick_thing_place = findViewById(R.id.pick_thing_place);
        pick_thing_tel = findViewById(R.id.pick_thing_tel);
        pick_thing_QQ = findViewById(R.id.pick_thing_QQ);
        pick_thing_VX = findViewById(R.id.pick_thing_VX);
        addphoto1 = findViewById(R.id.addphoto1);
        addphoto2 = findViewById(R.id.addphoto2);
        pick_thing_option = findViewById(R.id.pick_thing_option);
        submit_information = findViewById(R.id.submit_information);
        indicator_thing = findViewById(R.id.indicator_thing);
        find_time = findViewById(R.id.find_time);


        pick_thing_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Toast.makeText(I_pick_thing.this,checkedId+"",Toast.LENGTH_SHORT).show();
                mOption = checkedId;
            }
        });

        addphoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo1_Flag = 1;
                if (ContextCompat.checkSelfPermission(I_pick_thing.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(I_pick_thing.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    photo1_File = takePhoto(lostItem.getId() + "addphoto1.jpg", sTAKE_PHOTO1);//拍照并获取图片真实路径
                }
            }
        });

        addphoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo2_Flag = 1;
                if (ContextCompat.checkSelfPermission(I_pick_thing.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(I_pick_thing.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    photo2_File = takePhoto(lostItem.getId() + "addphoto2.jpg", sTAKE_PHOTO2);//拍照并获取图片真实路径
                }
            }
        });


        find_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DatapickerFragment());
            }
        });
        initSpinner();
        submit_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = pick_thing_name.getText().toString();
                mFounder = pick_thing_people.getText().toString();
                mTel = pick_thing_tel.getText().toString();
                mQQ = pick_thing_QQ.getText().toString();
                mWeChat = pick_thing_VX.getText().toString();
                mPickedPlace = pick_thing_place.getText().toString();
                mPickedDate = find_time.getText().toString();
                if (!(mName.equals("")) && !(mFounder.equals("")) && !(mPickedPlace.equals(""))
                        && !(mPickedDate.equals("添加拾取时间")) && mOption != 0 && photo1_File != null && photo2_File != null) {

                    if (!(mTel.equals("")) || !(mQQ.equals("")) || !(mWeChat.equals(""))) {
                        if(RegisterActivity.isNumeric(mTel)&&RegisterActivity.isNumeric(mQQ)) {
                            push_information();
                            //Toast.makeText(I_pick_thing.this,"第三个地方"+lostItem.getObjectId(),Toast.LENGTH_SHORT).show();
                        }else{
                            showDialog("请输入正确的电话或QQ号码",null,I_pick_thing.this);
                        }

                    } else {
                        showDialog("请完善您的联系方式！", null, I_pick_thing.this);
                    }
                } else {
                    showDialog("拾取物信息不完善！", null, I_pick_thing.this);
                }
//                if (Build.VERSION.SDK_INT >= 24) {
//                    String string=getFilesDir().toString();
//                    DeleteFileUtil.deleteDirectory(string);
//                } else {
//                   String string=getExternalCacheDir().toString();
//                    DeleteFileUtil.deleteDirectory(string);
//                }
           }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (photo1_Flag == 1) {
                        photo1_File = takePhoto(lostItem.getId() + "addphoto1.jpg", sTAKE_PHOTO1);//拍照并获取图片真实路径

                    }
                    if (photo2_Flag == 1) {
                        photo2_File = takePhoto(lostItem.getId() + "addphoto2.jpg", sTAKE_PHOTO2);//拍照并获取图片真实路径
                    }
                } else {
                    Toast.makeText(I_pick_thing.this, "无照相权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //所有信息上传云端同时发送推送
    private void push_information() {
        lostItem.setName(mName);
        lostItem.setLabel(mLabel);
        lostItem.setFounder(mFounder);
        lostItem.setTel(mTel);
        lostItem.setQQ(mQQ);
        lostItem.setWeChat(mWeChat);
        lostItem.setPickedPlace(mPickedPlace);
        lostItem.setPickedDate(mPickedDate);
        lostItem.setUserAccount(mUserAccount);
        lostItem.setOption(mOption);
        String filePath_1 = photo1_File.toString();
        String filePath_2 = photo2_File.toString();
        String filePath_3 = minPhoto_File.toString();
        final String[] filePaths = new String[3];
        filePaths[0] = filePath_1;
        filePaths[1] = filePath_2;
        filePaths[2] = filePath_3;

        ProgressDialog progressDialo = new ProgressDialog(I_pick_thing.this);
        progressDialo.setTitle("信息正在上传");
        progressDialo.setMessage("Loading...");
        progressDialo.show();

        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    lostItem.setImageA(list.get(0));
                    lostItem.setImageB(list.get(1));
                    lostItem.setImageThumbnail(list.get(2));
                    savePost();
                }

            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {


            }

            @Override
            public void onError(int i, String s) {

            }

            private void savePost() {
                if (BmobUser.isLogin()) {
                    //添加一对一关联，用户关联帖子
                    lostItem.setPerson(BmobUser.getCurrentUser(Person.class));
                    lostItem.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(I_pick_thing.this, "添加数据成功", Toast.LENGTH_SHORT).show();
                                ID = lostItem.getObjectId();
                                BmobPushManager bmobPushManager = new BmobPushManager();
                                BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
                                List<String> channels = new ArrayList<>();
                                //TODO 替换成你需要推送的所有频道，推送前请确认已有设备订阅了该频道，也就是channels属性存在该值
                                channels.add(mLabel);
                                query.addWhereContainedIn("channels", channels);
                                bmobPushManager.setQuery(query);
                                bmobPushManager.pushMessage("有人捡到" + mLabel + ",请检查是不是您的" + ID, new PushListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            // Toast.makeText(I_pick_thing.this, "成功", Toast.LENGTH_SHORT).show();;
                                        } else {
                                            Toast.makeText(I_pick_thing.this, "失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                finish();
                            } else {
                                Toast.makeText(I_pick_thing.this, "添加数据失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(I_pick_thing.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }


    //初始化Spinner
    private void initSpinner() {
        adapter = ArrayAdapter.createFromResource(I_pick_thing.this,
                R.array.indicator_strings, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indicator_thing.setAdapter(adapter);
        indicator_thing.setOnItemSelectedListener(this);
    }


    //显示提示框
    public static void showDialog(String title, String Message, Context context) {
        AlertDialog.Builder dia = new AlertDialog.Builder(context);
        dia.setTitle(title);
        dia.setMessage(Message);
        dia.setPositiveButton("确定", null);
        dia.show();
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
                        if (Build.VERSION.SDK_INT >= 24) {
                            minPhoto_File = new File(getFilesDir(), lostItem.getId() + "minphoto.jpg");
                        } else {
                            minPhoto_File = new File(getExternalCacheDir(), lostItem.getId() + "minphoto.jpg");
                        }
                        Log.d("minphoto文件", minPhoto_File.toString());
                        copyFile(photo1_File.toString(), minPhoto_File.toString());
                        if (Build.VERSION.SDK_INT >= 24) {
                            minimageUri = FileProvider.getUriForFile(I_pick_thing.this, "com.csti.eyefind.fileprovider", minPhoto_File);//第一个参数是一个context对象，第二个参数是任意唯一的字符串，第三个参数是刚刚创建的File对象
                            //Log.d("MainActivity", outputImage.toString() + "手机系统版本高于Android7.0");
                        } else {
                            //调用fromFile()方法将File对象转换成Uri对象，这个Uri对象标识着output_image.jpg这张图片的本地真实路径
                            //Log.d("MainActivity", outputImage.toString() + "手机系统版本低于Android7.0");
                            minimageUri = Uri.fromFile(minPhoto_File);
                        }
                        //调用BitmapFactory的decodeStream（）方法将output_image.jpg解析成Bitmap对象，然后把它设置到ImageView中显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Bitmap minbitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(minimageUri));
                        //Toast.makeText(I_pick_thing.this,"路径"+imageUri.getPath(),Toast.LENGTH_LONG).show();
                        Log.d("Uri名称", imageUri.toString());
                        Log.d("photo文件", photo1_File.toString());
                        //minPhoto_File = photo1_File;
                        compressPhoto(bitmap, photo1_File, 800);
                        compressPhoto(minbitmap, minPhoto_File, 200);
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
                        compressPhoto(bitmap, photo2_File, 800);
                        addphoto2.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    //图片压缩
    private static void compressPhoto(Bitmap bitmap, File photoFile, float size) {
        float ratio = size / bitmap.getHeight();
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * ratio), (int) size, false);
        try {
            FileOutputStream fos = new FileOutputStream(photoFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //拍照
    private File takePhoto(String photoName, int flag_TAKE_PHOTO) {
        //创建File对象，用于储存拍照后的图片
        File outputImage = null;
        if (Build.VERSION.SDK_INT >= 24) {
            outputImage = new File(getFilesDir(), photoName);
        } else {
            outputImage = new File(getExternalCacheDir(), photoName);
        }
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

    /*
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (!oldfile.exists()) { //文件不存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            //System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
}*/
    public boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }

            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */

            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
