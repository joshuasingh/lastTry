package com.example.lasttry.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.lasttry.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

    public static final String INTENT_FILTER = "INTENT_FILTER";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent intent=new Intent(INTENT_FILTER);
        intent.putExtra("url",remoteMessage.getData().get("message"));
        sendBroadcast(intent);
        showNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));

    }



    private void showNotification(String title, String message) {
        NotificationManager notificationManager=(NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        String NOTIFICAtION_CHANNEL_ID="com.example.lasttry.test";

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICAtION_CHANNEL_ID,
                     "Notification",NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("lasttry channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder
                (this,NOTIFICAtION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("info");


        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());






    }




    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }
}
