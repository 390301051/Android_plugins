package com.example.processp;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;

/**
 * Created by joker on 2020-9-25.
 * Description: 5.0以上的
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {
    private final int jobWakeUpId = 1;
    private JobScheduler jobScheduler;
    private JobInfo.Builder builder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("JobWakeUpService", "onStartCommand执行啦");
        builder = new JobInfo.Builder(jobWakeUpId, new ComponentName(this, JobWakeUpService.class))
                .setMinimumLatency(4000)
                .setPersisted(true);
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // 如果杀死了启动 轮询onStartJob
        Log.e("JobWakeUpService", "onStartJob 执行啦执行啦");

        //判断服务有没有在运行
        boolean messageServiceAlive = isServiceRunning(MyService.class.getName());
        if (!messageServiceAlive) {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                startForegroundService (new Intent (this,MyService.class));
            }else {
                startService(new Intent (this,MyService.class));
            }
        }

        //执行任务
        jobScheduler.schedule(builder.build());

        //第二个参数，类似于onStopJob的返回值，是否需要重新执行
        jobFinished(params, false);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * 判断某个服务是否在运行
     *
     * @param serviceName
     * @return
     */
    private boolean isServiceRunning(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}