package com.csti.eyefind.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.csti.eyefind.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class LostItemLab {
    private static LostItemLab sLostItemLab;
    private List<LostItem> mLostItems;
    private static List<Bitmap> bitmaps;

    public static LostItemLab getInstance(Context context){
        if(sLostItemLab==null){
            bitmaps=new ArrayList<>();
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img1));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img2));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img3));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img4));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img5));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img6));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img7));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img8));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img9));
            bitmaps.add(BitmapFactory.decodeResource(context.getResources(),R.drawable.img10));
            sLostItemLab=new LostItemLab(context);
        }
        return sLostItemLab;
    }

    private LostItemLab(Context context){
        mLostItems=new ArrayList<>();
        Random r=new Random();
        for(int i=0;i<30;i++){
            int j=r.nextInt(10);Log.d("mytag",j+"");
            LostItem lostItem=new LostItem();
            lostItem.setBitmapA(bitmaps.get(j));
            lostItem.setName("一根白色type-c数据线");
            lostItem.setPickedDate("2天前");
            lostItem.setPickedPlace("挂在C11门口球场篮筐顶上");
            mLostItems.add(lostItem);
        }
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

    public void addLostItem(LostItem c){
        mLostItems.add(c);
    }
}
