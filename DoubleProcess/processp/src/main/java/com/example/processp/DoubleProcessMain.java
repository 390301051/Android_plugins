package com.example.processp;

//        {
//          "type": "module",
//          "name": "Uni_DoubleProcess",
//          "class": "com.example.processp.DoubleProcessMain"
//        }

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

public class DoubleProcessMain extends WXModule {

    @JSMethod(uiThread = true)
    public void startProcess(JSCallback jsCallback){
        Activity activity = (Activity)mWXSDKInstance.getContext();
        Intent start=new Intent (activity,MyService.class);
        // 双进程守护
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            activity.startForegroundService (start);
            activity.startForegroundService (new Intent(activity, GuardService.class));
        }else {
            activity.startService(start);
            activity.startService(new Intent(activity, GuardService.class));
        }
        activity.startService(new Intent(activity, JobWakeUpService.class));
        JSONObject result = new JSONObject();
        result.put("result","双进程服务");
        jsCallback.invoke(result); //js回调方法
    }

}
