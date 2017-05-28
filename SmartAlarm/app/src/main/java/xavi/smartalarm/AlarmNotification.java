package xavi.smartalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Xavi and Reylin on 22/03/2017.
 */

public class AlarmNotification extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmService.class);
        String str = intent.getExtras().getString(context.getString(R.string.info));
        if(str != null) {
            if (str.equals(context.getString(R.string.sound))) {
                i.putExtra(context.getString(R.string.info), context.getString(R.string.sound));
                i.putExtra(context.getString(R.string.text),intent.getExtras().getString(context.getString(R.string.text)));
                context.startService(i);
            } else if(str.equals(context.getString(R.string.stop))) {
                context.stopService(i);
            } else {

            }
        }else{

        }


    }


    private void createStopButton() {


    }



}
