package com.csti.eyefind.activities;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class PhotoTest extends BmobObject {
    private BmobFile img;

    public void setImg(BmobFile img) {
        this.img = img;
    }

    public BmobFile getImg() {
        return img;
    }
}
