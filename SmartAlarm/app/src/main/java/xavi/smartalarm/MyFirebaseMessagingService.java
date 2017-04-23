package xavi.smartalarm;

/**
 * Created by Xavi on 23/4/17.
 */

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {

            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();

            Log.d(TAG, "NOTIFICATION RECEIVED\n Title: " + title + "\n Text: "+text);

            //Show notifications in the status bar
            showNotification(title, text);
        }
    }

    private void showNotification(String title, String text) {

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_24)
                        .setContentTitle(title)
                        .setContentText(text);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}


