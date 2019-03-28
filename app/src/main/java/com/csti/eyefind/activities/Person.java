package com.csti.eyefind.activities;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class Person extends BmobUser{
    private String mName;//用户姓名        //不需要
    private String mSex;//性别
    private String mCollege;//学院
    private String mMajor;//专业
    //private int mId;//学号                    ///变成了BmobUser的username
    private String mPassword;//用户密码       //不需要

    public String getmName() {
        return mName;
    }

    public String getmSex() {
        return mSex;
    }

    public String getmCollege() {
        return mCollege;
    }

    public String getmMajor() {
        return mMajor;
    }
/*
    public int getmId() {
        return mId;
    }
*/
    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmSex(String mSex) {
        this.mSex = mSex;
    }

    public void setmCollege(String mCollege) {
        this.mCollege = mCollege;
    }

    public void setmMajor(String mMajor) {
        this.mMajor = mMajor;
    }
/*
    public void setmId(int mId) {
        this.mId = mId;
    }
*/
    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
