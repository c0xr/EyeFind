package com.csti.eyefind.activities;

import android.content.Context;

public final class MeasureUtil {
    public static int dp2px(int dip, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip * scale + 0.5f);
    }
}
