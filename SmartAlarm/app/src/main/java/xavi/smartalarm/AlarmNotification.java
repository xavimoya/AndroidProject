package xavi.smartalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Xavi and Reylin on 22/03/2017.
 */

public class AlarmNotification extends BroadcastReceiver{

    public MediaPlayer player;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm activated")
                .setContentText("Alarm alarm alarm alarm alarm text")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentInfo("Information");


        player = MediaPlayer.create(context,R.raw.closer);

        player.start();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    public MediaPlayer getPlayer(){
        return player;
    }

    void pausePlayer(){
        player.pause();
    }

}
