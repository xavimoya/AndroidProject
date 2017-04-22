package xavi.smartalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class alarmService extends Service {

	private MediaPlayer player;
    private String textAlarm;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	


	@Override
	public void onDestroy() {
		Toast.makeText(this, "Alarma detenida", Toast.LENGTH_LONG).show();
        if (player != null) {
            player.stop();
        }

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
        String str = intent.getExtras().getString("info");
        textAlarm = intent.getExtras().getString("text");
        if (str != null) {
            if (str.equals("sound")) {

                Context context = getApplicationContext();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                builder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Alarm activated")
                        .setContentText(textAlarm)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setContentInfo("Information");


                player = MediaPlayer.create(context, R.raw.closer);

                player.start();
                player.setLooping(true);

                //createStopButton();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            }

        } else {
            Toast.makeText(this, "Servicio no identificado", Toast.LENGTH_LONG).show();
        }
        return startid;
    }
}