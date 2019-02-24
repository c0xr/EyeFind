package com.csti.eyefind.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.csti.eyefind.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LostItemLab {
    private static LostItemLab sLostItemLab;
    private Map<String,List<LostItem>> mMap;
    private String[] adapterTypes;

    public static LostItemLab getInstance(Context context){
        if(sLostItemLab==null){
            sLostItemLab=new LostItemLab(context);
        }
        return sLostItemLab;
    }

    private LostItemLab(Context context){
        adapterTypes=context.getResources().getStringArray(R.array.indicator_strings);
        mMap=new HashMap<>();
        for(int i=0;i<adapterTypes.length;i++){
            mMap.put(adapterTypes[i],new ArrayList<LostItem>());
        }
    }

    public List<LostItem> getLostItems(String adaperType) {
        return mMap.get(adaperType);
    }

    public LostItem getLostItem(String adpterType,UUID id){
        for(LostItem lostItem:mMap.get(adpterType)){
            if(lostItem.getId().equals(id)){

                return lostItem;
            }
        }
        return null;
    }

    public void clearList(String adapterType){
        if(mMap.get(adapterType).size()!=0) {
            mMap.get(adapterType).clear();
        }
    }
}