package com.example.windowheadtoast;
/*
2020.9.29
author：jie
company：csrd
* */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;

/**
 * 用于自定义一个Toast，模仿类似于QQ和微信来消息时候在顶部弹出的消息提示框
 */
public class WindowHeadToast implements  View.OnClickListener{
    private Context mContext;
    private View headToastView;
    private LinearLayout linearLayout=null;
    private LinearLayout.LayoutParams layoutParams;
    private WindowManager.LayoutParams wm_params;
    private Notifi notifi;
    private final static int ANIM_DURATION = 600;
    private final static int ANIM_DISMISSS_DURATION = 2000;
    private final static int ANIM_CLOSE = 20;
    private android.os.Handler mHander = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private WindowManager wm;
    private boolean flags;  //初始化时为true，addview，二次点击为false，update
    private int downX;
    private int downY;

    public WindowHeadToast(Context context) {
        mContext = context;
        notifi=new Notifi(context);
        flags=true;
    }

    public void showCustomToast(String msg) {
            if(flags){
                initHeadToastView(msg);
            }else {
                showMyview(msg);
            }
//        notifi.CreateNot(msg);
//        setHeadToastViewAnim();
        // 延迟10s后关闭
//        mHander.sendEmptyMessageDelayed(ANIM_CLOSE, 10000);
    }

    private void setHeadToastViewAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", -700, 0);
        animator.setDuration(ANIM_DURATION);
        animator.start();
    }

    private void animDismiss() {
        if (linearLayout == null || linearLayout.getParent() == null) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, -700);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }
        });
    }

    /**
     * 移除HeaderToast  (一定要在用户点击后移除，否则会导致程序崩溃，同时修改flags，下次触发重新初始化，否则无法update)
     */
     public void dismiss() {
         Log.e("isnull", String.valueOf(linearLayout!=null));
        if (null != linearLayout) {
            wm.removeView(linearLayout);
            notifi.removeNot();
            flags=true;
        }
    }

    public void initHeadToastView(String content) {

        //准备Window要添加的View
        linearLayout = new LinearLayout(mContext);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT );
        linearLayout.setLayoutParams(layoutParams);
        headToastView = View.inflate(mContext, R.layout.header_toast, null);
        // 为headToastView设置Touch事件
        RelativeLayout VIEW=(RelativeLayout) headToastView.findViewById(R.id.header_toast);
        VIEW.setOnClickListener(this);
        linearLayout.addView(headToastView);
        // 定义WindowManager 并且将View添加到WindowManagar中去
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
         wm_params = new WindowManager.LayoutParams();
        wm_params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕长亮
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        wm_params.height = LinearLayout.LayoutParams.WRAP_CONTENT;               //窗口的高
        wm_params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        wm_params.gravity= Gravity.TOP;
        wm_params.x = 0;
        wm_params.y = 20;
        wm_params.format = -3;  // 会影响Toast中的布局消失的时候父控件和子控件消失的时机不一致，比如设置为-1之后就会不同步
        wm_params.alpha = 1f;
        TextView lblTitle=(TextView)headToastView.findViewById(R.id.header_toast_content);
        TextView time=(TextView) headToastView.findViewById(R.id.header_toast_time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        time.setText(str);
        lblTitle.setText(content);
        wm.addView(linearLayout, wm_params);
        flags=false;
    }

    private void showMyview(String content) {
        TextView lblTitle=(TextView)headToastView.findViewById(R.id.header_toast_content);
        TextView time=(TextView) headToastView.findViewById(R.id.header_toast_time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        time.setText(str);
        lblTitle.setText(content);
        wm.updateViewLayout(linearLayout,wm_params);
    }

    @Override
    public void onClick(View v) {
             dismiss();
             Activity activity=(Activity)mContext;
             Intent intent=new Intent(mContext,activity.getClass());
             mContext.startActivity(intent);
    }
}
