package com.csti.eyefind.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.csti.eyefind.R;

public class DetailActivity extends AppCompatActivity {
    private final static String EXTRA_LOSTITEM="lostItem";
    private LostItem mLostItem;

    public static Intent newIntent(Context packageContext,LostItem lostItem){
        Intent intent=new Intent(packageContext,DetailActivity.class);
        intent.putExtra(EXTRA_LOSTITEM,lostItem);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mLostItem=(LostItem) getIntent().getSerializableExtra(EXTRA_LOSTITEM);
    }
}
