package com.example.itemlocator;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * This class it responsible for creating a notification.
 */
public class FireNotification extends Service {
    public FireNotification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //Get a reference to the manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //A reference to a notification object
        Notification notification;

        //Creating a pending intent to open this activity when the notification is clicked

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);


        //API 26 and newer, use "Channels" for notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Declare channel information
            String channel = "1111";
            CharSequence name = "Chanel1111";
            int importanceLevel = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel notificationChannel = new NotificationChannel(channel, name, importanceLevel);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setChannelId(channel)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setTicker("Ticker notify")
                    .setContentTitle("New Item Locator Notification")
                    .setContentText("It's been a while!")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("We've noticed that you haven't added any new products to your app recently! Why not add one now!"))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            notification = builder.build();


        } else {
            //Old way, without channels

            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setTicker("Ticker notify")
                    .setContentText("We've noticed that you haven't added any new products to your app recently! Why not add one now!")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            notification = builder.build();

        }


        //The id can be used if you need to identify between several different notifications
        notificationManager.notify(1, notification);
    }
}
