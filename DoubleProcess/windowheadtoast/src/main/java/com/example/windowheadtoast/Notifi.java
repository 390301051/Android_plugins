package com.example.windowheadtoast;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/*
2020.9.29
author：jie
company：csrd
method：在系统栏新增通知消息，锁屏可见,
* */
public class Notifi {
    private int notifiid=111;
    private String channel_id="Emergency";
    private Context mcontext;
    private NotificationManager manager;

    public Notifi(Context context){
        mcontext=context;
    }
    public void CreateNot(String content){
        manager= (NotificationManager)mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel Channel = null;
        Channel = new NotificationChannel(channel_id,"Emergency", NotificationManager.IMPORTANCE_HIGH);
        Channel.enableLights(true);//设置提示灯
        Channel.setLightColor(Color.RED);//设置提示灯颜色
        Channel.setShowBadge(true);//显示logo
        Channel.setDescription("CZDZ");//设置描述
        Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
        manager.createNotificationChannel(Channel);

        Activity activity=(Activity)mcontext;
        Intent  notificationIntent=new Intent(mcontext,activity.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0, notificationIntent, 0);
        Notification.Builder  notification=new Notification.Builder(mcontext,channel_id)
                    .setLargeIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.czdzicon))
                    .setContentTitle("CZDZ地震消息")
                    .setContentText(content)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.czdzicon)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
                manager.notify(notifiid,notification.build());
    }
    /*
    移除通知
    * */
    public void removeNot(){
        manager.cancel(notifiid);
    }
}
