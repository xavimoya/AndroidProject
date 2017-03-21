package xavi.smartalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Reylin on 21/03/2017.
 */

class CustomAlarmAdapter extends ArrayAdapter<Alarm> {

    CustomAlarmAdapter(Context context, Alarm[] alarms) {
        super(context, R.layout.custome_row, alarms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater alarmsInflater = LayoutInflater.from(getContext());
        View customView = alarmsInflater.inflate(R.layout.custome_row, parent, false);

        Alarm alarm = getItem(position);
        TextView alarmTextView = (TextView) customView.findViewById(R.id.text_title);
        TextView time = (TextView) customView.findViewById(R.id.text_time);

        alarmTextView.setText(alarm.getTitle());
        time.setText(alarm.getDate().get(Calendar.HOUR_OF_DAY) + " : " + alarm.getDate().get(Calendar.MINUTE));

        return customView;
    }
}
