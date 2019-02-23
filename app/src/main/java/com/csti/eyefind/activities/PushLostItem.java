package com.csti.eyefind.activities;

import cn.bmob.v3.BmobObject;

public class PushLostItem extends BmobObject {
    private Person mPerson;//关联表用
    private String mLabel;//标签(类别)
    private String mName;//名称

    public Person getmPerson() {
        return mPerson;
    }

    public String getmName() {
        return mName;
    }

    public void setmPerson(Person mPerson) {
        this.mPerson = mPerson;
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
