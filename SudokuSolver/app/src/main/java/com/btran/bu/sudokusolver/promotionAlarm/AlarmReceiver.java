package com.btran.bu.sudokusolver.promotionAlarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.btran.bu.sudokusolver.MainActivity;
import com.btran.bu.sudokusolver.R;

/**
 * Trigger the alarm when we receive the broadcast
 */
public class AlarmReceiver extends BroadcastReceiver
{
    private static final int COME_BACK_NOTIFICATION_ID = 001;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // creating action for the notification to bring the user back to the Sudoku Application
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // hold reference to the notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // create notification
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.sudoku_logo)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_body))
                .setAutoCancel(true); // hide the notification once the user interacts with it

        // set the pending intent to return the user back to the Sudoku Application
        notificationBuilder.setContentIntent(resultPendingIntent);

        // trigger the notification
        notificationManager.notify(COME_BACK_NOTIFICATION_ID, notificationBuilder.build());
    }
}
