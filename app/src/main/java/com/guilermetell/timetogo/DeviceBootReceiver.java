package com.guilermetell.timetogo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by guilherme.ms on 4/13/2016.
 */
public class DeviceBootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int entranceHour = PreferenceManager.getDefaultSharedPreferences(context).getInt(HomeActivity.SELECTED_HOUR, HomeActivity.UNKNOWN_TIME);
            int entranceMinute = PreferenceManager.getDefaultSharedPreferences(context).getInt(HomeActivity.SELECTED_MINUTE, HomeActivity.UNKNOWN_TIME);
            int workHour = PreferenceManager.getDefaultSharedPreferences(context).getInt(HomeActivity.SELECTED_WORK_HOUR, 8);
            int workMinute = PreferenceManager.getDefaultSharedPreferences(context).getInt(HomeActivity.SELECTED_WORK_MINUTE, 13);
            int outHour = (entranceHour + workHour + 1);
            int outMinute = (entranceMinute + workMinute);
            if (outMinute > 60) {
                outMinute = entranceMinute + workMinute - 60;
                outHour++;
            }
            if (outHour > 24)
                outHour -= 24;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, outHour);
            calendar.set(Calendar.MINUTE, outMinute);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, pendingIntent);
        }
    }
}
