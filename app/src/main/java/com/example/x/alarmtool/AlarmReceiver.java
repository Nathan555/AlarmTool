package com.example.x.alarmtool;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.net.Uri;
import android.provider.Settings;


/**
 * Created by X on 2015-07-13.
 */
public class AlarmReceiver extends BroadcastReceiver {

    String title, text;
    public AlarmReceiver()
    {

        title = "Default";
        text = "";
    }

    public AlarmReceiver(String _title, String _text)
    {
        title = _title;
        text =  _text;
    }
    @Override
    public void onReceive(Context c, Intent i) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(c)
                        .setSmallIcon(R.drawable.ic_alarm_24dp)
                        .setContentTitle(i.getStringExtra("Title"))
                        .setContentText(text)
                        .setSound(  Settings.System.DEFAULT_NOTIFICATION_URI);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) c.getSystemService(c.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        c.unregisterReceiver(this);
    }
}