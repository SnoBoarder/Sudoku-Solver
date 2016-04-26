package com.btran.bu.sudokusolver.promotionAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Service to schedule an alarm to trigger a notification
 */
public class AlarmService
{
    private Context _context;
    private PendingIntent _alarmSender;

    public AlarmService(Context context)
    {
        _context = context;
        // create the pending intent, which simply triggers the AlarmReceiver intent
        _alarmSender = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
    }

    /**
     * Start the alarm to trigger the notification
     *
     * @param delayMillis the amount of time to wait until triggering the notification
     */
    public void startAlarm(int delayMillis)
    {
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, _alarmSender);
    }
}
