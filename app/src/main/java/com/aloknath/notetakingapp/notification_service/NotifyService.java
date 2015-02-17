package com.aloknath.notetakingapp.notification_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import com.aloknath.notetakingapp.R;
import com.aloknath.notetakingapp.activities.TaskDisplayActivity;

/**
 * Created by ALOKNATH on 2/16/2015.
 */

public class NotifyService extends Service {

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private Bundle bundle;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        public void run() {

            // Do something here
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if(bundle != null) {

                Intent intent1 = new Intent(NotifyService.this.getApplicationContext() , TaskDisplayActivity.class);
                intent1.putExtras(bundle);

                PendingIntent contentIntent = PendingIntent.getActivity(NotifyService.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new Notification.Builder(NotifyService.this)
                        .setContentTitle(bundle.getString("title"))
                        .setContentText(bundle.getString("description"))
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(contentIntent)
                        .setSound(sound)
                        .build();


                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(bundle.getInt("notificationId"), notification);
            }
            System.out.println("BACK_GROUND SERVICE IS RUNNING");
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            if(intent.getExtras() != null) {
                this.bundle = intent.getExtras();
            }
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

}
