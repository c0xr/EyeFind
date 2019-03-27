package com.csti.eyefind.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.csti.eyefind.R;

import cn.bmob.v3.BmobUser;

public class MainOverviewFragment extends Fragment {
    private String mUserAccount;//用户账号//学号
    private FragmentManager mFm;
    private Fragment mFragment;
    private MeasureUtil.Timer mTimer;
    private FloatingActionButton mFab;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainOverviewFragment() {
        // Required empty public constructor
    }

    public static MainOverviewFragment newInstance(String param1, String param2) {
        MainOverviewFragment fragment = new MainOverviewFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_overview, container, false);
        mFm=getActivity().getSupportFragmentManager();
        mFragment=mFm.findFragmentById(R.id.pager_fragment_contianer);

        if (mFragment == null) {
            mFragment=PagerFragment.newInstance();
            mFm.beginTransaction()
                    .add(R.id.pager_fragment_contianer,mFragment)
                    .commit();
        }

//        ActionBar actionBar= getActivity().getActionBar();
//        actionBar.setElevation(0);
//        actionBar.setSubtitle(getResources().getString(R.string.overview_activity_subtitle));

        mFab= view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BmobUser.isLogin()){
                    startAnimationB();
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            Intent intent=new Intent(getActivity(),I_push_thing.class);
                            startActivity(intent);
                        }
                    }, 300);
                }else{
                    com.csti.eyefind.activities.I_pick_thing.showDialog("请登录!", null, getActivity());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_overview,menu);
        mTimer=new MeasureUtil.Timer(1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh) {
            if (!mTimer.isReady()) {
                return true;
            }
            LostItemLab.getInstance(getActivity()).clearAll();
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
    public void onResume() {
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
