package com.csti.eyefind.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class ImageLoaderDetail {
    private Handler mNetworkHandler;
    private String mObjectId;
    private LostItem mLostItem;

    public ImageLoaderDetail(Handler networkHandler,LostItem lostItem) {
        mNetworkHandler = networkHandler;
        mLostItem = lostItem;
        mObjectId=lostItem.getObjectId();
    }

    public void load(){
        BmobQuery<LostItem> bmobQuery=new BmobQuery<>();
        bmobQuery.getObject(mObjectId, new QueryListener<LostItem>() {
            @Override
            public void done(final LostItem lostItem, BmobException e) {
                if(e==null){
                    new Thread(){
                        @Override
                        public void run() {
                            mLostItem.setPickedPlace(lostItem.getPickedPlace());
                            mLostItem.setFounder(lostItem.getFounder());
                            mLostItem.setPickedDate(lostItem.getPickedDate());
                            mLostItem.setTel(lostItem.getTel());
                            mLostItem.setQQ(lostItem.getQQ());
                            mLostItem.setWeChat(lostItem.getWeChat());
                            mLostItem.setOption(lostItem.getOption());
                            mLostItem.setUserAccount(lostItem.getUserAccount());
                            mLostItem.setBitmapA(getBitmap(lostItem.getImageA().getUrl()));
                            mLostItem.setBitmapB(getBitmap(lostItem.getImageB().getUrl()));
                            mNetworkHandler.sendEmptyMessage(0);
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
