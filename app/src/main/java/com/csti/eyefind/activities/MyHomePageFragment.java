package com.csti.eyefind.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static android.content.Context.MODE_PRIVATE;

public class MyHomePageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView user_name, user_sex, user_college, user_major, user_id;
    private Button exit;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyHomePageFragment() {
        // Required empty public constructor
    }

    public static MyHomePageFragment newInstance(String param1, String param2) {
        MyHomePageFragment fragment = new MyHomePageFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_home_page, container, false);
        Bmob.initialize(getActivity(), "a744c289f17c26d9df110a2fa115feaf");
        user_college = view.findViewById(R.id.user_college);
        user_id = view.findViewById(R.id.user_id);
        user_major = view.findViewById(R.id.user_major);
        user_name = view.findViewById(R.id.user_name);
        user_sex = view.findViewById(R.id.user_sex);
        exit = view.findViewById(R.id.exit);
        String objectId = ((MainActivity)getActivity()).getObjectId();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("objectId",objectId);
        editor.apply();
        BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
        bmobQuery.getObject(objectId, new QueryListener<Person>() {
            @Override
            public void done(Person object, BmobException e) {
                if (e == null) {
                    user_college.setText(object.getmCollege());
                    user_id.setText(object.getUsername() + "");//
                    user_major.setText(object.getmMajor());
                    user_name.setText(object.getmName());//
                    user_sex.setText(object.getmSex());
                } else {
                    //toast("查询失败：" + e.getMessage());
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dia = new AlertDialog.Builder(getActivity());
                dia.setTitle("是否要退出登录");
                dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
                        editor.putString("objectId","000");
                        editor.apply();
                        Person user = BmobUser.getCurrentUser(Person.class);
                        BmobQuery<LostItem> query = new BmobQuery<>();
                        query.addWhereEqualTo("mPerson", user);
                        query.findObjects(new FindListener<LostItem>() {

                            @Override
                            public void done(List<LostItem> object, BmobException e) {
                                if(e==null){
                                    //Toast.makeText(UserActivity.this,"成功"+object.size(),Toast.LENGTH_SHORT).show();
                                    String[] Lostthing=new String[object.size()];
                                    for(int i=0;i<object.size();i++){
                                        Lostthing[i]=object.get(i).getLabel();
                                    }
                                    BmobInstallationManager.getInstance().unsubscribe(Arrays.asList(Lostthing), new InstallationListener<BmobInstallation>() {
                                        @Override
                                        public void done(BmobInstallation bmobInstallation, BmobException e) {
                                            if (e == null) {
                                                //Toast.makeText(UserActivity.this, "批量取消订阅成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(getActivity(),"失败",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        BmobUser.logOut();
                        ((MainActivity) Objects.requireNonNull(getActivity())).setObjectId(null);
                        ((MainActivity) Objects.requireNonNull(getActivity())).changeToLogInFragment();
                    }
                });
                dia.setNegativeButton("取消",null);
                dia.show();

            }
        });

        view.findViewById(R.id.textView7).getLayoutParams().height = getActivity().getWindowManager().getDefaultDisplay().getHeight()/2;
        return view;
    }

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

    public void changeUserViewIfo(Person person, String getObjectId){
        user_college.setText(person.getmCollege());
        user_id.setText(person.getUsername() + "");//
        user_major.setText(person.getmMajor());
        user_name.setText(person.getmName());//
        user_sex.setText(person.getmSex());
        String objectId = getObjectId;
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("objectId",objectId);
        editor.apply();
    }

    public void saveIfo(String getObjectId){
        String objectId = getObjectId;
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("objectId",objectId);
        editor.apply();
    }
}
