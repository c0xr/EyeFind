package com.csti.eyefind.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
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
import es.dmoral.toasty.Toasty;

public class I_push_thing extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner indicator_push_thing;
    private ArrayAdapter<CharSequence> adapter;
    private EditText push_lostitem;
    private FloatingActionButton push;
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
                    startAnimationB();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            savePost();
                        }
                    },300);
                }else{
                    I_pick_thing.showDialog("请输入物品名称！",null,I_push_thing.this);
                }
            }
        });

        ViewTreeObserver vto = push.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                push.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startAnimationA();
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
                        BmobInstallationManager.getInstance().subscribe(Arrays.asList(mLabel), new InstallationListener<BmobInstallation>() {
                            @Override
                            public void done(BmobInstallation bmobInstallation, BmobException e) {
                                if (e == null) {
                                    Toasty.success(I_push_thing.this,"挂失提交成功",Toasty.LENGTH_SHORT).show();
//                                    Toast.makeText(I_push_thing.this,"成功",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toasty.error(I_push_thing.this,"挂失失物失败",Toasty.LENGTH_SHORT).show();
                                }
                            }
                        });

                        finish();
                    } else {
                        Toasty.error(I_push_thing.this,"提交失败，请检查网络",Toasty.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toasty.error(I_push_thing.this,"请先登录",Toasty.LENGTH_SHORT).show();
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

    @SuppressLint("RestrictedApi")
    private void startAnimationA(){
        int endX=push.getLeft();
        int startX=endX+400;
        int endY=push.getTop();
        int midY=endY-200;
        push.setVisibility(View.VISIBLE);
        ObjectAnimator animatorX = ObjectAnimator
                .ofFloat(push, "x", startX, endX)
                .setDuration(700);

        ObjectAnimator animatorYA = ObjectAnimator
                .ofFloat(push, "y", endY, midY)
                .setDuration(350);
        animatorYA.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator animatorYB = ObjectAnimator
                .ofFloat(push, "y", midY, endY)
                .setDuration(350);
        animatorYB.setInterpolator(new BounceInterpolator());
        animatorYB.setStartDelay(350);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX,animatorYA,animatorYB);
        animatorSet.start();
    }

    @SuppressLint("RestrictedApi")
    private void startAnimationB(){
        int startX=push.getLeft();
        int endX=startX+400;
        push.setVisibility(View.VISIBLE);
        ObjectAnimator animatorX = ObjectAnimator
                .ofFloat(push, "x", startX, endX)
                .setDuration(300);
        animatorX.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animatorR = ObjectAnimator
                .ofFloat(push, "rotation", 0, 360)
                .setDuration(300);
        animatorX.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX,animatorR);
        animatorSet.start();
    }
}
