package com.example.processp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Intent mIntent = new Intent(context,MyService.class);
        context.startService(mIntent);
    }
}