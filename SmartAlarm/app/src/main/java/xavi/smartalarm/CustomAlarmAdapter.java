package xavi.smartalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Reylin on 14/03/2017.
 */

class CustomAlarmAdapter extends ArrayAdapter<Alarm>{

    CustomAlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, R.layout.custome_row, alarms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater alarmsInflater = LayoutInflater.from(getContext());
        View customView = alarmsInflater.inflate(R.layout.custome_row, parent, false);

        Alarm alarm = getItem(position);
        TextView alarmTextView = (TextView) customView.findViewById(R.id.text_title);
        TextView time = (TextView) customView.findViewById(R.id.text_time);
        TextView date = (TextView) customView.findViewById(R.id.text_date);


        alarmTextView.setText(alarm.getTitle());
        time.setText(alarm.getDate().get(Calendar.HOUR_OF_DAY) + ":" + alarm.getDate().get(Calendar.MINUTE));
        date.setText(alarm.getDate().get(Calendar.DAY_OF_MONTH) +"/" + alarm.getDate().get(Calendar.MONTH) + "/" + alarm.getDate().get(Calendar.YEAR));

        return customView;
    }
}
