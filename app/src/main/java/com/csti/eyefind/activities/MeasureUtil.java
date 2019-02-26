package com.csti.eyefind.activities;

import android.content.Context;

public final class MeasureUtil {
    public static int dp2px(int dip, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip * scale + 0.5f);
    }

    public static class Timer{
        private long mLastTime;
        private long mTimeGap;

        public Timer(long timeGap) {
            mTimeGap = timeGap;
            mLastTime=System.currentTimeMillis();
        }

        public boolean isReady(){
            long currentTime=System.currentTimeMillis();
            if(currentTime-mLastTime>mTimeGap){
                mLastTime=currentTime;
                return true;
            }
            return false;
        }
    }
}
