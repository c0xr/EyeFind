package com.csti.eyefind.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.csti.eyefind.R;

import java.sql.Time;

public class OverviewActivity extends AppCompatActivity {
    private String mUserAccount;//用户账号//学号
    private FragmentManager mFm;
    private Fragment mFragment;
    private MeasureUtil.Timer mTimer;

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

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                String objectId = sharedPreferences.getString("objectId", "");
                mUserAccount = sharedPreferences.getString("account", "");
                if (!(objectId.equals(" "))) {
                    Intent intent=new Intent(OverviewActivity.this,I_push_thing.class);
                    startActivity(intent);
                }else {
                    com.csti.eyefind.activities.I_pick_thing.showDialog("请登录!", null, OverviewActivity.this);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview,menu);
        mTimer=new MeasureUtil.Timer(3000);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!mTimer.isReady()){
            return true;
        }
        mFm.beginTransaction()
                .remove(mFragment)
                .commit();
        mFragment=PagerFragment.newInstance();
        mFm.beginTransaction()
                .add(R.id.pager_fragment_contianer,mFragment)
                .commit();
        return true;
    }
}
