package com.aloknath.notetakingapp.notification_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.aloknath.notetakingapp.activities.MainActivity;
import com.aloknath.notetakingapp.activities.NoteEditorActivity;
import com.aloknath.notetakingapp.R;

/**
 * Created by ALOKNATH on 2/16/2015.
 */

public class NotifyService extends Service {

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;

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

            Intent intent1 = new Intent(NotifyService.this.getApplicationContext() , MainActivity.class);

            PendingIntent contentIntent = PendingIntent.getActivity(NotifyService.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(NotifyService.this)
                    .setContentTitle("Title")
                    .setContentText("Description")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(contentIntent)
                    .setSound(sound)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notification);
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
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

}


//public class NotifyService extends Service {
//
//    Intent valuesPassed;
//
//    @Override
//    public IBinder onBind(Intent intent) {
////        valuesPassed = intent;
////        Bundle bundle = intent.getExtras();
////        Toast.makeText(this, bundle.getString("title"), Toast.LENGTH_SHORT).show();
////        Toast.makeText(this, (String)bundle.get("description"), Toast.LENGTH_SHORT).show();
////        Toast.makeText(this, (String)bundle.get("location"), Toast.LENGTH_SHORT).show();
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
////
////        valuesPassed = intent;
////        Bundle bundle = intent.getExtras();
////        Toast.makeText(this, bundle.getString("title"), Toast.LENGTH_SHORT).show();
////        Toast.makeText(this, (String)bundle.get("description"), Toast.LENGTH_SHORT).show();
////        Toast.makeText(this, (String)bundle.get("location"), Toast.LENGTH_SHORT).show();
//
//
//
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        String path = "android.resource://" + getPackageName() + "/" + R.raw.alarm;
////        Uri soundPath = Uri.parse(path);
//
//        NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        //Notification notification = new Notification(R.drawable.ic_drawer, "Notify Alarm Start", System.currentTimeMillis());
//        Intent intent1 = new Intent(this.getApplicationContext() , NoteEditorActivity.class);
//        //if(valuesPassed != null) {
////            Bundle bundle = valuesPassed.getExtras();
////
////            intent.putExtra("title", (String)bundle.get("title"));
////            intent.putExtra("description", (String)bundle.get("description"));
////            intent.putExtra("location", (String)bundle.get("location"));
////        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new Notification.Builder(this)
////                    .setContentTitle((String)bundle.get("title"))
////                    .setContentText((String)bundle.get("description"))
//                .setContentTitle("Title")
//                .setContentText("Description")
//                .setSmallIcon(R.drawable.ic_drawer)
//                .setContentIntent(contentIntent)
//                .setSound(sound)
//                .build();
//
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
////        NotificationCompat.Builder mBuilder =
////                new NotificationCompat.Builder(this)
////                        .setSmallIcon(R.drawable.ic_drawer)
////                        .setContentTitle("My notification")
////                        .setContentText("Hello World!");
//
//        notificationManager.notify(0, notification);
//
//        return super.onStartCommand(intent, flags, startId);
//
//    }
//
//    @Override
//    public void onCreate() {
////
////        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//////        String path = "android.resource://" + getPackageName() + "/" + R.raw.alarm;
//////        Uri soundPath = Uri.parse(path);
////
////        NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////        //Notification notification = new Notification(R.drawable.ic_drawer, "Notify Alarm Start", System.currentTimeMillis());
////        Intent intent = new Intent(this.getApplicationContext() , NoteEditorActivity.class);
////        //if(valuesPassed != null) {
//////            Bundle bundle = valuesPassed.getExtras();
//////
//////            intent.putExtra("title", (String)bundle.get("title"));
//////            intent.putExtra("description", (String)bundle.get("description"));
//////            intent.putExtra("location", (String)bundle.get("location"));
////        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
////
////            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
////            Notification notification = new Notification.Builder(this)
//////                    .setContentTitle((String)bundle.get("title"))
//////                    .setContentText((String)bundle.get("description"))
////                    .setContentTitle("Title")
////                    .setContentText("Description")
////                    .setSmallIcon(R.drawable.ic_drawer)
////                    .setContentIntent(contentIntent)
////                    .setSound(sound)
////                    .build();
////
////        notification.flags |= Notification.FLAG_AUTO_CANCEL;
////
//////        NotificationCompat.Builder mBuilder =
//////                new NotificationCompat.Builder(this)
//////                        .setSmallIcon(R.drawable.ic_drawer)
//////                        .setContentTitle("My notification")
//////                        .setContentText("Hello World!");
////
////            notificationManager.notify(0, notification);
//       // }
//    }
//
//    //    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////
////        NotificationManager  notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////        Notification notification = new Notification(R.drawable.ic_drawer, "Notify Alarm Start", System.currentTimeMillis());
////        Intent intent = new Intent(this , MainActivity.class);
////        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
////        notification.setLatestEventInfo(this, "Notify label", "Notify text", contentIntent);
////        notificationManager.notify(NOTIFICATION, notification);
////    }
//
//}
