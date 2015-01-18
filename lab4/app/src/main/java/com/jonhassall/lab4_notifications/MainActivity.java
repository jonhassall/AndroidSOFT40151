package com.jonhassall.lab4_notifications;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createNotification(View view) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent notificationIntent = new Intent(this, NotificationReceiverActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent deleteNotificationIntent = new Intent(this, DeleteNotificationReceiverActivity.class);
        PendingIntent notificationDeletePendingIntent = PendingIntent.getActivity(this, 0, deleteNotificationIntent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("New notification")
                .setContentText("Subject").setSmallIcon(R.drawable.bike_whiteonblack)
                .setDeleteIntent(notificationDeletePendingIntent)
                .setContentIntent(notificationPendingIntent)
                .addAction(R.drawable.bike_whiteonblack, "Call", notificationPendingIntent)
                .addAction(R.drawable.bike_whiteonblack, "More", notificationPendingIntent)
                .addAction(R.drawable.bike_whiteonblack, "And more", notificationPendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

    public void clearNotifications(View view) {
        //Clear current notifications
        Log.d("Click", "Clear notifications pressed");
        NotificationManager notifManager= (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
