package com.csti.eyefind.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;


import com.csti.eyefind.R;

import java.util.Calendar;

public class DatapickerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datapicker_fragment, container, false);
        Calendar calendar = Calendar.getInstance();
        I_pick_thing activity=(I_pick_thing)getActivity();
        showDatePickerDialog(getActivity(),activity.getFind_time(),calendar);
        return view;
    }
    public static void showDatePickerDialog(Activity activity, final Button button, Calendar calendar) {
        // Calendar 需要这样来得到
        // Calendar calendar = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity,
                // 绑定监听器(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        button.setText( + year + "年" + monthOfYear+1
                                + "月" + dayOfMonth + "日");
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
