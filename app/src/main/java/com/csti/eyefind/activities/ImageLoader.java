package com.csti.eyefind.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ImageLoader {
    private String mAdapterType;
    private Handler mNetworkHandler;
    private List<LostItem> mLostItems;

    public ImageLoader(String adapterType, Handler networkHandler, List<LostItem> lostItems) {
        mAdapterType = adapterType;
        mNetworkHandler = networkHandler;
        mLostItems = lostItems;
    }

    public void load(){
        BmobQuery<LostItem> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("mLabel",mAdapterType);
        bmobQuery.findObjects(new FindListener<LostItem>() {
            @Override
            public void done(final List<LostItem> list, BmobException e) {
                if(e==null){
                    new Thread(){
                        @Override
                        public void run() {
                            for(LostItem _lostItem:list){
                                LostItem lostItem=new LostItem();
                                lostItem.setName(_lostItem.getName());
                                lostItem.setPickedDate(_lostItem.getPickedDate());
                                lostItem.setPickedPlace(_lostItem.getPickedPlace());
                                lostItem.setBitmapA(getBitmap(_lostItem.getImageA().getUrl()));
                                mLostItems.add(lostItem);
                                Log.d("mytag",lostItem.getBitmapA()+"");
                                mNetworkHandler.sendMessage(new Message());
                            }
                        }
                    }.start();
                }else{
                    Log.d("mytag",e+"");
                }
            }
        });
    }

    private Bitmap getBitmap(String path){
        Bitmap bitmap=null;
        try{
            URL url=new URL(path);
            URLConnection connection=url.openConnection();
            connection.connect();
            InputStream inputStream=connection.getInputStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
