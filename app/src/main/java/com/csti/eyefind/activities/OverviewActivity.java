package com.csti.eyefind.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.csti.eyefind.R;

public class OverviewActivity extends AppCompatActivity {
    public static Intent newIntent(Context packageContext){
        return new Intent(packageContext, OverviewActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.pager_fragment_contianer);

        if (fragment == null) {
            fragment=PagerFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.pager_fragment_contianer,fragment)
                    .commit();
        }

        ActionBar actionBar= getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setSubtitle(getResources().getString(R.string.overview_activity_subtitle));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview,menu);
        return true;
    }
}
