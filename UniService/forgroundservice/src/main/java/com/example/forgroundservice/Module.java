package com.example.forgroundservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class Module extends WXModule {

    @JSMethod(uiThread = true)
    public  void startService(){
        Activity activity = (Activity)mWXSDKInstance.getContext();
        Intent start=new Intent (activity,MyService.class);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            activity.startForegroundService(start);
        }else {
            activity.startService(start);
        }
    }
}
