package com.mercadolibre.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.mercadolibre.activities.MainActivity;
import com.mercadolibre.activities.R;

/**
 * Created by bweinberg on 26/02/14.
 */
public class NotificationService extends IntentService{


    public NotificationService() {
        super("NotificationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        showNotification();

    }

    private void showNotification() {
        CharSequence text = getText(R.string.service_started);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("isNotification", "isNotification");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setTicker(text);
        builder.setContentText(text);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(123, builder.build());


    }
}
