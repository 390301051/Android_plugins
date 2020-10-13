package com.example.windowheadtoast;


import com.example.windowheadtoast.RomUtils.FloatWindowManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;


public class windowToast extends WXModule{
    private WindowHeadToast windowHeadToast = new WindowHeadToast(mWXSDKInstance.getContext());
    @JSMethod(uiThread = true)
    public  void openWindow(String content){
        windowHeadToast.showCustomToast(content); //"地址：四川阿坝藏族自治州金川县，震级7.7，经度：101.99，纬度：31.11"
    }
    @JSMethod(uiThread = true)
    public void checkpremission(){
        FloatWindowManager.getInstance().applyOrShowFloatWindow(mWXSDKInstance.getContext());
    }
}
