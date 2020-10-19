package com.example.windowheadtoast;
/*
*  {
      "hooksClass": "",
      "plugins": [
        {
          "type": "module",
          "name": "Uni_joker_Windowheadtoast",
          "class": "com.example.windowheadtoast.windowToast"
        }
      ]
    },*/

import android.content.Context;

import com.example.windowheadtoast.RomUtils.FloatWindowManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.bridge.JSCallback;

public class windowToast extends WXModule{
    public static Context context;
    public static WindowHeadToast windowHeadToast;
    @JSMethod(uiThread = true)
    public void inithandle(JSCallback jsCallback){
        this.context=mWXSDKInstance.getContext();
        windowHeadToast=new WindowHeadToast(context);
        JSONObject result = new JSONObject();
        result.put("result",true);
        jsCallback.invoke(result); //js回调方法
    }
    @JSMethod(uiThread = true)
    public  void openWindow(JSONObject options, JSCallback jsCallback){
//        WindowHeadToast windowHeadToast=new WindowHeadToast(mWXSDKInstance.getContext());
        String content=options.getString("content");
        JSONObject result = new JSONObject();
        result.put("result","开启悬浮框");
        jsCallback.invoke(result); //js回调方法
        windowHeadToast.showCustomToast(content); //"地址：四川阿坝藏族自治州金川县，震级7.7，经度：101.99，纬度：31.11"
    }
    @JSMethod(uiThread = true)
    public void checkpremission(JSCallback jsCallback){
        JSONObject result = new JSONObject();
        result.put("result","权限检查");
        jsCallback.invoke(result); //js回调方法
        FloatWindowManager.getInstance().applyOrShowFloatWindow(mWXSDKInstance.getContext());
    }
}
