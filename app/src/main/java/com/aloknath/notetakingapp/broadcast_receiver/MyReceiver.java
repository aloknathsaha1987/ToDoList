package com.aloknath.notetakingapp.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aloknath.notetakingapp.notification_service.NotifyService;

/**
 * Created by ALOKNATH on 2/16/2015.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, NotifyService.class);
        context.startService(service1);
    }
}
