package com.csti.eyefind.activities;

import android.graphics.Bitmap;

import java.util.UUID;

public class LostItem {
    private UUID mId;//用于识别LostItem对象的Id,在构造方法中已经初始化
    private Bitmap mBitmapA;//图片1
    private Bitmap mBitmapB;//图片2
    private String mName;//名称
    private String mLabel;//标签(类别)
    private String mFounder;//拾取人
    private String mTel;//电话
    private String mQQ;//QQ
    private String mWeChat;//微信
    private String mPickedPlace;//拾取地点
    private String mPickedDate;//拾取日期
    private int mOption;//转交他人
    private String mUserAccount;//用户账号

    public LostItem() {
        mId=UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public Bitmap getBitmapA() {
        return mBitmapA;
    }

    public void setBitmapA(Bitmap bitmapA) {
        mBitmapA = bitmapA;
    }

    public Bitmap getBitmapB() {
        return mBitmapB;
    }

    public void setBitmapB(Bitmap bitmapB) {
        mBitmapB = bitmapB;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getFounder() {
        return mFounder;
    }

    public void setFounder(String founder) {
        mFounder = founder;
    }

    public String getTel() {
        return mTel;
    }

    public void setTel(String tel) {
        mTel = tel;
    }

    public String getQQ() {
        return mQQ;
    }

    public void setQQ(String QQ) {
        mQQ = QQ;
    }

    public String getWeChat() {
        return mWeChat;
    }

    public void setWeChat(String weChat) {
        mWeChat = weChat;
    }

    public String getPickedPlace() {
        return mPickedPlace;
    }

    public void setPickedPlace(String pickedPlace) {
        mPickedPlace = pickedPlace;
    }

    public String getPickedDate() {
        return mPickedDate;
    }

    public void setPickedDate(String pickedDate) {
        mPickedDate = pickedDate;
    }

    public int getOption() {
        return mOption;
    }

    public void setOption(int option) {
        mOption = option;
    }

    public String getUserAccount() {
        return mUserAccount;
    }

    public void setUserAccount(String userAccount) {
        mUserAccount = userAccount;
    }
}
