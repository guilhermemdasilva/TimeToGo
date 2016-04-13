package com.guilermetell.timetogo;

/**
 * Created by guilherme.ms on 4/13/2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        Toast.makeText(context, "TIME TO GO!!!", Toast.LENGTH_SHORT).show();
        int icon = R.mipmap.ic_launcher;
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle("TIME TO GO!!!")
                .setContentText("TIME TO GOOOOOO!!!").build();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(9999, notification);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(5000);
    }
}