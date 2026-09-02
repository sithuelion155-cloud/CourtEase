package com.sithu.courtease.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sithu.courtease.R;

public class CourtEaseMessagingService
        extends FirebaseMessagingService {

    private static final String TAG = "CourtEaseFCM";
    private static final String CHANNEL_ID = "courtease_notifications";

    @Override
    public void onMessageReceived(
            @NonNull RemoteMessage remoteMessage) {

        Log.d(
                TAG,
                "Message received: "
                        + remoteMessage.getMessageId()
        );

        String title = "CourtEase";

        String body =
                "You have a new notification.";

        if (remoteMessage.getNotification() != null) {

            if (remoteMessage.getNotification().getTitle() != null) {
                title =
                        remoteMessage.getNotification().getTitle();
            }

            if (remoteMessage.getNotification().getBody() != null) {
                body =
                        remoteMessage.getNotification().getBody();
            }
        }

        showNotification(title, body);
    }

    @Override
    public void onNewToken(@NonNull String token) {

        Log.d(
                TAG,
                "New FCM token generated: " + token
        );

        // Later we can save the token to the user's Firestore document.
    }

    private void showNotification(
            String title,
            String body) {

        NotificationManager manager =
                (NotificationManager)
                        getSystemService(
                                NOTIFICATION_SERVICE
                        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "CourtEase Notifications",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        this,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                R.drawable.ic_launcher_foreground
                        )
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setPriority(
                                NotificationCompat.PRIORITY_DEFAULT
                        );

        manager.notify(
                (int) System.currentTimeMillis(),
                builder.build()
        );
    }
}