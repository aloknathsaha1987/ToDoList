package com.aloknath.notetakingapp.notification_manager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.aloknath.notetakingapp.MainActivity;
import com.aloknath.notetakingapp.NoteEditorActivity;
import com.aloknath.notetakingapp.R;

/**
 * Created by ALOKNATH on 2/16/2015.
 */
public class NotifyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        String path = "android.resource://" + getPackageName() + "/" + R.raw.alarm;
//        Uri soundPath = Uri.parse(path);

        NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //Notification notification = new Notification(R.drawable.ic_drawer, "Notify Alarm Start", System.currentTimeMillis());
        Intent intent = new Intent(this.getApplicationContext() , NoteEditorActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Pending Task")
                .setContentText("Task to be Done")
                .setSmallIcon(R.drawable.ic_drawer)
                .setContentIntent(contentIntent)
                .setSound(sound)
                .build();

//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_drawer)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!");

        notificationManager.notify(1, notification);
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = new Notification(R.drawable.ic_drawer, "Notify Alarm Start", System.currentTimeMillis());
//        Intent intent = new Intent(this , MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        notification.setLatestEventInfo(this, "Notify label", "Notify text", contentIntent);
//        notificationManager.notify(NOTIFICATION, notification);
//    }

}
