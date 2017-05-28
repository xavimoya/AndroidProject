package xavi.smartalarm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import xavi.smartalarm.Object.Alarm;
import xavi.smartalarm.R;

/*
 * Created by Xavi and Reylin on 14/03/2017.
 */

public class CustomAlarmAdapter extends ArrayAdapter<Alarm>{

    public CustomAlarmAdapter(Context context, ArrayList<Alarm> alarms) {
        super(context, R.layout.custome_row, alarms);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater alarmsInflater = LayoutInflater.from(getContext());
        View customView = alarmsInflater.inflate(R.layout.custome_row, parent, false);

        Alarm alarm = getItem(position);
        TextView alarmTextView = (TextView) customView.findViewById(R.id.text_title);
        TextView time = (TextView) customView.findViewById(R.id.text_time);
        TextView date = (TextView) customView.findViewById(R.id.text_date);
        TextView location = (TextView) customView.findViewById(R.id.text_location);

        Switch sw = (Switch)customView.findViewById(R.id.switch1);
        sw.setChecked(true);


        assert alarm != null;
        alarmTextView.setText(alarm.getTitle());

        int minutes = alarm.getDate().get(Calendar.MINUTE);
        int hours = alarm.getDate().get(Calendar.HOUR_OF_DAY);
        String text;
        if (hours<10)text="0"+hours + ":";
        else text = hours+":";
        if (minutes < 10)text = text + ("0"+minutes);
        else text = text + (String.valueOf(minutes));
        time.setText(text);
        date.setText(alarm.getDate().get(Calendar.DAY_OF_MONTH) +"/" + (alarm.getDate().get(Calendar.MONTH)+1) + "/" + alarm.getDate().get(Calendar.YEAR));
        location.setText(alarm.getDestiny());


        return customView;
    }
}
