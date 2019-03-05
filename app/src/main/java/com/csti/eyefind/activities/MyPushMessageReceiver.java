package com.csti.eyefind.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.csti.eyefind.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.BmobPush;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyPushMessageReceiver extends BroadcastReceiver {
    private final int pushId = 1;
    public static final String PRIMARY_CHANNEL = "default";
    private LostItem mlostItem;
    private static final int PUSH_NOTIFICATION_ID = (0x001);
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";

    @Override
    public void onReceive(final Context context, Intent intent) {


        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            //Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            String jsonStr = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            String real_info = null;
            String objectId = null;
            //处理Json的数据，将其转化为正常string类型
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                real_info = jsonObject.getString("alert");
                objectId = real_info;
                real_info = real_info.substring(0, real_info.indexOf("的") + 1);
                objectId = objectId.substring(objectId.indexOf("的") + 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("通知ID",objectId);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Intent intent1 =DetailActivity.newIntent(context,objectId,false);
            intent1.putExtra("objectid", objectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentTitle("失物提交信息")//设置通知栏标题
                    .setContentIntent(pi) //设置通知栏点击意图
                    .setContentText(real_info)
                    .setTicker(real_info) //通知首次出现在通知栏，带上升动画效果的
                    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                    .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                    .setChannelId(PUSH_CHANNEL_ID)
                    .setDefaults(Notification.DEFAULT_ALL);

            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            if (notificationManager != null) {
                notificationManager.notify(PUSH_NOTIFICATION_ID, notification);
            }

            Log.d("bmob", "客户端收到推送内容：" + intent.getStringExtra("msg"));
        }
    }
}
