package com.example.processp;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.processp.ProcessConnection;

/**
 * 守护进程
 */
public class GuardService extends Service {
    private static final String TAG=GuardService.class.getSimpleName();
    private final int GuardId = 0;
    private static final String CHANNEL_ID = "CZDZGuard";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        NotificationChannel Channel = new NotificationChannel(CHANNEL_ID,"CZDZService",NotificationManager.IMPORTANCE_HIGH);
//        Channel.enableLights(true);//设置提示灯
//        Channel.setLightColor(Color.RED);//设置提示灯颜色
//        Channel.setShowBadge(true);//显示logo
//        Channel.setDescription("CZDZ");//设置描述
//        Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
//        manager.createNotificationChannel(Channel);
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        Notification  notification=new Notification.Builder(this,CHANNEL_ID)
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher))
//                .setContentTitle("CZDZGuard")
//                .setContentText("正在监测地震数据...")
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent)
//                .build();
//        startForeground(GuardId,notification);
        //提高进程的优先级
        startForeground(GuardId, new Notification());
        //绑定建立连接
        bindService(new Intent(this, MyService.class), mServiceConnection, Context.BIND_IMPORTANT);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {

            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }
        };
    }


    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(GuardService.this, "与Myservice建立连接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(GuardService.this, MyService.class));
            //绑定建立连接
            bindService(new Intent(GuardService.this, MyService.class), mServiceConnection,
                    Context.BIND_IMPORTANT);

        }
    };
    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }
}