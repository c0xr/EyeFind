package com.csti.eyefind.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csti.eyefind.R;


public class MyPropertyContentFragment extends Fragment {
    private View view;
    private static final String KEY = "title";
    private TextView tvContent;
    private static Fragment[] fragments = new Fragment[]{
            new MyGetPropertyFragment(),
            new MyLostPropertyFragment(),
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_property_content,container,false);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        String string = getArguments().getString(KEY);
        tvContent.setText(string);
        tvContent.setTextColor(Color.BLUE);
        tvContent.setTextSize(30);
        return view;
    }

    /**
     * fragment静态传值
     */
    public static Fragment newInstance(int i ,String str){
        Bundle bundle = new Bundle();
        bundle.putString(KEY,str);
        fragments[i].setArguments(bundle);

        return fragments[i];
    }
}
