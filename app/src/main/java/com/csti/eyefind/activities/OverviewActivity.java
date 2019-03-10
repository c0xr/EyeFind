package com.csti.eyefind.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.csti.eyefind.R;

import cn.bmob.v3.BmobUser;

public class OverviewActivity extends AppCompatActivity {
    private String mUserAccount;//用户账号//学号
    private FragmentManager mFm;
    private Fragment mFragment;
    private MeasureUtil.Timer mTimer;
    private FloatingActionButton mFab;

    public static Intent newIntent(Context packageContext){
        return new Intent(packageContext, OverviewActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mFm=getSupportFragmentManager();
        mFragment=mFm.findFragmentById(R.id.pager_fragment_contianer);

        if (mFragment == null) {
            mFragment=PagerFragment.newInstance();
            mFm.beginTransaction()
                    .add(R.id.pager_fragment_contianer,mFragment)
                    .commit();
        }

        ActionBar actionBar= getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setSubtitle(getResources().getString(R.string.overview_activity_subtitle));

        mFab=findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BmobUser.isLogin()){
                    startAnimationB();
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            Intent intent=new Intent(OverviewActivity.this,I_push_thing.class);
                            startActivity(intent);
                        }
                    }, 300);
                }else{
                    com.csti.eyefind.activities.I_pick_thing.showDialog("请登录!", null, OverviewActivity.this);
                }
            }
        });

        ViewTreeObserver vto = mFab.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startAnimationA();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview,menu);
        mTimer=new MeasureUtil.Timer(1000);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh) {
            if (!mTimer.isReady()) {
                return true;
            }
            LostItemLab.getInstance(this).clearAll();
            mFm.beginTransaction()
                    .remove(mFragment)
                    .commit();
            mFragment = PagerFragment.newInstance();
            mFm.beginTransaction()
                    .add(R.id.pager_fragment_contianer, mFragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimationA();
    }

    private void startAnimationA(){
        int endX=mFab.getLeft();
        int startX=endX+400;
        int endY=mFab.getTop();
        int midY=endY-200;
        mFab.setVisibility(View.VISIBLE);
        ObjectAnimator animatorX = ObjectAnimator
                .ofFloat(mFab, "x", startX, endX)
                .setDuration(700);

        ObjectAnimator animatorYA = ObjectAnimator
                .ofFloat(mFab, "y", endY, midY)
                .setDuration(350);
        animatorYA.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator animatorYB = ObjectAnimator
                .ofFloat(mFab, "y", midY, endY)
                .setDuration(350);
        animatorYB.setInterpolator(new BounceInterpolator());
        animatorYB.setStartDelay(350);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX,animatorYA,animatorYB);
        animatorSet.start();
    }

    private void startAnimationB(){
        int startX=mFab.getLeft();
        int endX=startX+400;
        mFab.setVisibility(View.VISIBLE);
        ObjectAnimator animatorX = ObjectAnimator
                .ofFloat(mFab, "x", startX, endX)
                .setDuration(300);
        animatorX.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator animatorR = ObjectAnimator
                .ofFloat(mFab, "rotation", 0, 360)
                .setDuration(300);
        animatorX.setInterpolator(new AccelerateInterpolator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX,animatorR);
        animatorSet.start();
    }
}
