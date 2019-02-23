package com.csti.eyefind.activities;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LostItemLab {
    private static LostItemLab sLostItemLab;
    private List<LostItem> mLostItems;
    private static List<Bitmap> bitmaps;

    public static LostItemLab getInstance(Context context){
        if(sLostItemLab==null){
            sLostItemLab=new LostItemLab();
        }
        return sLostItemLab;
    }

    private LostItemLab(){
        mLostItems=new ArrayList<>();
    }

    public List<LostItem> getLostItems() {
        return mLostItems;
    }

    public LostItem getLostItem(UUID id){
        for(LostItem LostItem:mLostItems){
            if(LostItem.getId().equals(id)){
                return LostItem;
            }
        }
        return null;
    }
}