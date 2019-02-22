package com.csti.eyefind.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csti.eyefind.R;
import com.flyco.tablayout.SlidingTabLayout;

public class PagerFragment extends Fragment {
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private String[] mIndicatorStrings;

    public static PagerFragment newInstance() {

        Bundle args = new Bundle();

        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndicatorStrings=getResources().getStringArray(R.array.indicator_strings);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_pager,container,false);
        mViewPager=v.findViewById(R.id.pager);
        mSlidingTabLayout=v.findViewById(R.id.indicator);
        updateUI();
        return v;
    }

    private void updateUI(){
        FragmentManager fm=getActivity().getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                String[] adapterTypes=getResources().getStringArray(R.array.indicator_strings);
                String adapterType=adapterTypes[position];
                return ItemListFragment.newInstance(adapterType);
            }

            @Override
            public int getCount() {
                return mIndicatorStrings.length;
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager,mIndicatorStrings);
    }
}
