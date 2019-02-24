package com.csti.eyefind.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.csti.eyefind.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, NotificationActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, 0);

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
            NotificationManager manager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification notification= new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)//设置推送消息的图标
                    .setContentTitle("EyeFind") //设置推送标题
                    .setContentText(real_info)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();//设置推送内容
            manager.notify(1,notification);//设置通知栏
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
        }
    }
}
