package com.mercadolibre.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.mercadolibre.activities.R;

/**
 * Created by bweinberg on 26/02/14.
 */
public class ExampleService extends IntentService{

    //counter that operates as a generator of sequential numbers for the id
    static int notificationCounter = 0;


    public ExampleService() {
        super("ExampleService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        showNotification();

    }

    private void showNotification() {
        CharSequence text = getText(R.string.service_started);

        Notification mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentText(text)
                .setContentTitle("Mercadolibre")
                .setTicker(text)
                .setAutoCancel(true)
                .setLights(R.color.white, 1000, 5000)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .build();

        mBuilder.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        mBuilder.defaults |= Notification.DEFAULT_LIGHTS;

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancelAll();
        mNotificationManager.notify(notificationCounter, mBuilder);
        notificationCounter++;


    }
}
