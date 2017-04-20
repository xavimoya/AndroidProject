package xavi.smartalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.widget.Button;

/**
 * Created by Xavi and Reylin on 22/03/2017.
 */

public class AlarmNotification extends BroadcastReceiver{

    public MediaPlayer player;

    @Override
    public void onReceive(Context context, Intent intent) {


     /*   if(intent.getStringExtra("info").equals("sound")){
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
            player.setLooping(true);

            createStopButton();

            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1,builder.build());
        }else{
            player.stop();

        }*/

        Intent i = new Intent(context, alarmService.class);
        String str = intent.getExtras().getString("info");
        if(str != null) {
            if (str.equals("sound")) {
                i.putExtra("info", "sound");
                context.startService(i);
            } else if(str.equals("stop")) {
                context.stopService(i);
            } else {

            }
        }else{

        }


    }

    private void createStopButton() {


    }


    private void pausePlayer(){
        player.pause();
    }

}
