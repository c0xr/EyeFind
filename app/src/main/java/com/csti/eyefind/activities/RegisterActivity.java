package com.csti.eyefind.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.csti.eyefind.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //视频
    private MyVideoView mSignUpVideoView;

    private EditText register_name, register_college, register_major, register_id, register_password, register_password1;
    Spinner register_sex;
    private String input_sex;
    private ArrayAdapter<CharSequence> adapter;
    private Button register_push;
    public String[][] college_major = {{"计算机科学技术学院", "计算机科学与技术", "物联网"},
            {"软件学院", "软件工程"},
            {"历史学院", "历史学", "旅游管理", "考古学"},
            {"化学院", "高分子材料与工程", "应用化学", "化学专业", "化学工程与工艺", "环境科学", "制药工程"},
            {"电子工程学院", "通信工程", "电子信息工程", "电子科学与技术", "电子信息科学与技术", "自动化"},
            {"经济与工商管理学院", "会计学", "经济学", "国际经济与贸易", "金融学", "工商管理", "市场营销", "人力资源管理"},
            {"生命科学学院", "生物技术", "生物工程", "食品科学与工程", "生物制药"},
            {"文学院", "汉语言文学", "汉语国际教育"},
            {"西语学院", "英语专业", "商务英语", "法语专业", "德语专业"},
            {"东语学院", "朝鲜语专业", "阿拉伯语专业"},
            {"数据科学与技术学院", "数据科学与大数据技术", "网络空间安全"}};
    private String[] college = {"计算机科学技术学院", "软件学院", "历史学院", "化学院", "生命科学学院",
            "文学院", "西语学院", "东语学院", "电子工程学院", "经济与工商管理学院", "数据科学与技术学院"};
    private String[] major = {"计算机科学与技术", "物联网", "软件工程", "历史学", "旅游管理",
            "考古学", "高分子材料与工程", "应用化学", "化学专业", "化学工程与工艺", "环境科学",
            "制药工程", "通信工程", "电子信息工程", "电子科学与技术", "电子信息科学与技术",
            "自动化", "会计学", "经济学", "国际经济与贸易", "金融学", "工商管理", "市场营销",
            "人力资源管理", "生物技术", "生物工程", "食品科学与工程", "生物制药", "汉语言文学",
            "汉语国际教育", "英语专业", "商务英语", "法语专业", "德语专业", "朝鲜语专业", "阿拉伯语专业", "数据科学与大数据技术", "网络空间安全"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        //视频
//        mSignUpVideoView = (MyVideoView) findViewById(R.id.videoView_log_up);
//        mSignUpVideoView.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight();
//        String path = "android.resource://" + this.getPackageName() + "/" + R.raw.sighup;
//        mSignUpVideoView.setVideoPath(path);
//        mSignUpVideoView.start();
//        mSignUpVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//                mp.setLooping(true);
//            }
//        });

        register_name = findViewById(R.id.register_name);
        register_sex = findViewById(R.id.register_sex);
        register_college = findViewById(R.id.register_college);
        register_id = findViewById(R.id.register_id);
        register_major = findViewById(R.id.register_major);
        register_sex = findViewById(R.id.register_sex);
        register_password = findViewById(R.id.register_password);
        register_password1 = findViewById(R.id.register_password1);
        register_push = findViewById(R.id.register_push);
        adapter = ArrayAdapter.createFromResource(RegisterActivity.this,
                R.array.register_strings, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        register_sex.setAdapter(adapter);
        //注册监听器
        register_sex.setOnItemSelectedListener(this);

        register_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_college = register_college.getText().toString();
                register_college.setText(findRealInformation(input_college, college));
                String input_major = register_major.getText().toString();
                register_major.setText(findRealInformation(input_major, major));

                int flag = 0;
                //判断学院专业是否正确
                if (register_major.getText().toString().equals("wrong") || register_college.getText().toString().equals("wrong")) {
                    AlertDialog.Builder dia = new AlertDialog.Builder(RegisterActivity.this);
                    dia.setTitle("请输入正确的学院或专业！");
                    dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            register_college.setText("");
                            register_major.setText("");
                        }
                    });
                    dia.show();
                } else {
                    for (int i = 0; i < college_major.length; i++) {
                        if (register_college.getText().toString().equals(college_major[i][0])) {
                            for (int j = 1; j < college_major[i].length; j++) {
                                if (register_major.getText().toString().equals(college_major[i][j])) {
                                    flag = 1;
                                    final String input_name = register_name.getText().toString();
                                    final String input_password = register_password.getText().toString();
                                    final String input_password1 = register_password1.getText().toString();
                                    //判断学号是否为数字,是否为空
                                    if (isNumeric(register_id.getText().toString()) && !(register_id.getText().toString().equals(""))) {
                                        final int input_id = Integer.parseInt(register_id.getText().toString());
                                        if (input_name.equals("")) {
                                            AlertDialog.Builder dia = new AlertDialog.Builder(RegisterActivity.this);
                                            dia.setTitle("请输入姓名！");
                                            dia.setPositiveButton("确定", null);
                                            dia.show();
                                        } else {
                                            //判断两次密码是否相等以及两次密码是否都为空
                                            if (input_password.equals(input_password1) && !(input_password.equals("")) && !(input_password1.equals(""))) {
                                                AlertDialog.Builder dia = new AlertDialog.Builder(RegisterActivity.this);
                                                dia.setTitle("请确认您的信息！");
                                                dia.setMessage("学院：" + register_college.getText().toString() + "\n" + "专业：" + register_major.getText().toString());
                                                dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Person p2 = new Person();
                                                        p2.setmName(input_name);////////////////////////////////////////////////
                                                        p2.setmSex(input_sex);
                                                        p2.setmCollege(register_college.getText().toString());
                                                        p2.setmMajor(register_major.getText().toString());
                                                        p2.setUsername(input_id + "");          /////////////////////////////////
                                                        p2.setPassword(input_password);////////////////////////////////
                                                        p2.setmPassword(input_password);/////////////////
                                                        p2.signUp(new SaveListener<Person>() {
                                                            @Override
                                                            public void done(Person user, BmobException e) {
                                                                if (e == null) {
                                                                    finish();
                                                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(RegisterActivity.this, "注册失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                                dia.setNegativeButton("取消", null);
                                                dia.show();
                                            } else {
                                                I_pick_thing.showDialog("请确认密码输入无误！", null, RegisterActivity.this);
                                            }

                                        }

                                    } else {
                                        I_pick_thing.showDialog("请输入正确的学号！", null, RegisterActivity.this);
                                    }
                                }
                            }
                            if (flag != 1) {
                                I_pick_thing.showDialog("您的学院与专业不匹配！", null, RegisterActivity.this);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        input_sex = (String) register_sex.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public static String findRealInformation(String input, String[] information) {
        float Max_similarity = 0;
        String real_information = "";
        for (int i = 0; i < information.length; i++) {
            float get_similarity = levenshtein(input, information[i]);
            if (get_similarity >= Max_similarity) {
                Max_similarity = get_similarity;
                real_information = information[i];
            }
        }
        if (Max_similarity == 0) {
            return "wrong";
        } else {
            return real_information;
        }
    }

    public static float levenshtein(String str1, String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }
        //System.out.println("字符串\""+str1+"\"与\""+str2+"\"的比较");
        //取数组右下角的值，同样不同位置代表不同字符串的比较
        //System.out.println("差异步骤："+dif[len1][len2]);
        //计算相似度
        float similarity = 1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
        return similarity;
        //System.out.println("相似度："+similarity);
    }


    //得到最小值
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
