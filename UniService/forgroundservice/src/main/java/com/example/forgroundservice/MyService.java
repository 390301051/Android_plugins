package com.example.forgroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service  {
    private static final String TAG=MyService.class.getSimpleName();
    private static final String CHANNEL_ID = "CZDZService";
    public MyService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel Channel = null;
        Channel = new NotificationChannel(CHANNEL_ID,"CZDZService", NotificationManager.IMPORTANCE_HIGH);
        Channel.enableLights(true);//设置提示灯
        Channel.setLightColor(Color.RED);//设置提示灯颜色
        Channel.setShowBadge(true);//显示logo
        Channel.setDescription("CZDZ");//设置描述
        Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
        manager.createNotificationChannel(Channel);
        Notification notification = null;
            notification = new Notification.Builder(this,CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher))
                    .setContentTitle("CZDZ")
                    .setContentText("正在监测地震数据")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            startForeground(1,notification);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId){
        Log.d(TAG, "onStartCommand()");
        return START_STICKY;
//        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy()");
        stopForeground(true);
        super.onDestroy();
    }
}
