package com.csti.eyefind.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.csti.eyefind.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyPushMessageReceiver extends BroadcastReceiver {
    private final int pushId = 1;
    public static final String PRIMARY_CHANNEL = "default";
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, NotificationActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, 0);
        int  mNotificationId = hashCode();
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            //Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            String jsonStr = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            String real_info = null;
            String objectId=null;
            //处理Json的数据，将其转化为正常string类型
            try {
                JSONObject jsonObject=new JSONObject(jsonStr);
                real_info=jsonObject.getString("alert");
                objectId=real_info;
                real_info=real_info.substring(0,real_info.indexOf("的")+1);
                objectId=objectId.substring(objectId.indexOf("的")+1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*NotificationManager manager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification notification= new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)//设置推送消息的图标
                    .setContentTitle("EyeFind") //设置推送标题
                    .setContentText(real_info)
                    //.setChannelId("zhuhai")//适配8.0
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();//设置推送内容
            manager.notify(1,notification);//设置通知栏
            */
            /*
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.mipmap.ic_launcher);
            } else {
                builder.setSmallIcon(R.mipmap.ic_launcher);
            }
            builder.setContentTitle("EyeFind")
                    .setTicker(real_info)
                    .setContentText(real_info)
                    .setContentIntent(pi)
                    .setChannelId("zhuhai")//适配8.0
                    .setAutoCancel(true);//用户点击就自动消失
                    //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
            //适配8.0
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT >= 26){
                NotificationChannel channel = new NotificationChannel("zhuhai", "EyeFind", NotificationManager.IMPORTANCE_HIGH);
                builder.setChannelId("zhuhai");
                manager.createNotificationChannel(channel);
            }
            manager.notify(mNotificationId, builder.build());//每次改变mNotificationId的值才能在通知栏产生盖楼的效果
*/

/*
            private final int pushId = 1;
            public static final String PRIMARY_CHANNEL = "default";

            NotificationManager mNotificationManager = (NotificationManager) Utils.getContext().getSystemService(Utils.getContext().NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder;
            //判断是否是8.0Android.O
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
                        "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);
                chan1.setLightColor(Color.GREEN);
                chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                mNotificationManager.createNotificationChannel(chan1);
                mBuilder = new NotificationCompat.Builder(Utils.getContext(), PRIMARY_CHANNEL);
            } else {
                mBuilder = new NotificationCompat.Builder(Utils.getContext());
            }
            Intent notificationIntent = new Intent(Utils.getContext(), MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(Utils.getContext(), 0,
                    notificationIntent, 0);
            mBuilder.setContentTitle(senderStr)//设置通知栏标题
                    .setContentText(contentStr)//设置通知栏内容
                    .setContentIntent(intent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                    .setTicker(senderStr + ":" + contentStr) //通知首次出现在通知栏，带上升动画效果的
                    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                    .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                    .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
            Notification notify = mBuilder.build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(pushId, notify);
*/
           /* NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String id = "channel_01";
            CharSequence name = context.getString(R.string.app_name);
            String description = real_info;
            if(Build.VERSION.SDK_INT >= 26){
                NotificationChannel notificationChannel =
                        new NotificationChannel(id,name,NotificationManager.IMPORTANCE_HIGH);
            }*/

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder;
            //判断是否是8.0Android.O
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
                        "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);
                chan1.setLightColor(Color.GREEN);
                chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                mNotificationManager.createNotificationChannel(chan1);
                mBuilder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL);
            } else {
                mBuilder = new NotificationCompat.Builder(context);
            }
            mBuilder.setContentTitle("EyeFind")//设置通知栏标题
                    .setContentText(real_info)//设置通知栏内容
                    .setContentIntent(pi) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                    .setTicker(real_info) //通知首次出现在通知栏，带上升动画效果的
                    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                    .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                    .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
            Notification notify = mBuilder.build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(pushId, notify);


            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
        }
    }
}
