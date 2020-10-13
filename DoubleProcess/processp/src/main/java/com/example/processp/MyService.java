package com.example.processp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import com.example.processp.ProcessConnection;

import java.util.Calendar;

public class MyService extends Service {
    private static final String TAG=MyService.class.getSimpleName();
    private static final String CHANNEL_ID = "CZDZService";
    //阻止CPU休眠后，程序在后台被杀
//    private PowerManager pm = (PowerManager)getSystemService(this.POWER_SERVICE);
    private PowerManager.WakeLock mWakeLock = null;

    public MyService() {

    }

    @Override
    public void onCreate(){
        super.onCreate();
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel Channel = new NotificationChannel(CHANNEL_ID,"CZDZService",NotificationManager.IMPORTANCE_HIGH);
        Channel.enableLights(true);//设置提示灯
        Channel.setLightColor(Color.RED);//设置提示灯颜色
        Channel.setShowBadge(true);//显示logo
        Channel.setDescription("CZDZ");//设置描述
        Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
        manager.createNotificationChannel(Channel);

        Intent intent = getPackageManager().getLaunchIntentForPackage("io.dcloud.CZDZ");
//        Intent notificationIntent = new Intent(this,((Activity)this.getBaseContext()).getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification  notification=new Notification.Builder(this,CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.czdzicon))
                .setContentTitle("CZDZ")
                .setContentText("正在监测地震数据...")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.czdzicon)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        startForeground(1,notification);
        getLock(this);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId){
        Log.d(TAG, "onStartCommand()");
        bindService(new Intent(this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MessageBind();
    }
    private class MessageBind extends ProcessConnection.Stub {


        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(MyService.this, "与Guard建立连接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(MyService.this, GuardService.class));
            //绑定建立连接
            bindService(new Intent(MyService.this, GuardService.class), mServiceConnection,
                    Context.BIND_IMPORTANT);

        }
    };
   /* * 同步方法   得到休眠锁
     * @param context
     * @return
             */
    synchronized private void getLock(Context context) {
        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, MyService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis((System.currentTimeMillis()));
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 23 || hour <= 6) {
                mWakeLock.acquire(5000);
            } else {
                mWakeLock.acquire(300000);
            }
        }
        Log.v(TAG, "get lock");
    }

    synchronized private void releaseLock()
    {
        if(mWakeLock!=null){
            if(mWakeLock.isHeld()) {
                mWakeLock.release();
                Log.v(TAG,"release lock");
            }

            mWakeLock=null;
        }
    }
    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy()");
        releaseLock();
        stopForeground(true);
        super.onDestroy();
    }
}
