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


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, alarmService.class);
        String str = intent.getExtras().getString("info");
        if(str != null) {
            if (str.equals("sound")) {
                i.putExtra("info", "sound");
                i.putExtra("text",intent.getExtras().getString("text"));
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



}
