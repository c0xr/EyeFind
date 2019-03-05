package com.csti.eyefind.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.csti.eyefind.R;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;


public class MainPrimeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private VideoView mVideoView;

    public MainPrimeFragment() {
        // Required empty public constructor
    }

    public static MainPrimeFragment newInstance(String param1, String param2) {
        MainPrimeFragment fragment = new MainPrimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main_prime, container, false);
        Bmob.initialize(getActivity(), "a744c289f17c26d9df110a2fa115feaf");
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    //Toast.makeText(MainActivity.this, "该设备已经在设备表注册", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("你妈死了",e.getMessage());
                }
            }
        });
        // 启动推送服务
        BmobPush.startWork(getActivity());


        view.findViewById(R.id.I_pick_thing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
//                String objectId = sharedPreferences.getString("objectId", "");
//                mUserAccount = sharedPreferences.getString("account", "");
//                if (!(objectId.equals(" "))) {
                if(BmobUser.isLogin()){
                    Intent intent = new Intent(getActivity(), I_pick_thing.class);
                    startActivity(intent);
                }else {
                    com.csti.eyefind.activities.I_pick_thing.showDialog("请登录!", null, getActivity());
                }

            }
        });

        //test
        view.findViewById(R.id.I_will_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OverviewActivity.newIntent(getActivity()));
            }
        });

        //视频
        mVideoView = view.findViewById(R.id.videoView);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.s10;
        mVideoView.setVideoPath(path);
        mVideoView.start();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
