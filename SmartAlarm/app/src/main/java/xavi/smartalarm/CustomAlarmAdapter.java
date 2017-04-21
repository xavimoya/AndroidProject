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
 * Created by Xavi and Reylin on 14/03/2017.
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
        TextView location = (TextView) customView.findViewById(R.id.text_location);



        alarmTextView.setText(alarm.getTitle());

        int minutes = alarm.getDate().get(Calendar.MINUTE);
        int hours = alarm.getDate().get(Calendar.HOUR_OF_DAY);
        String text = "";
        if (hours<10)text="0"+hours + ":";
        else text = hours+":";
        if (minutes < 10)text.concat("0"+minutes);
        else text.concat(String.valueOf(minutes));
        time.setText(text);
        date.setText(alarm.getDate().get(Calendar.DAY_OF_MONTH) +"/" + alarm.getDate().get(Calendar.MONTH)+1 + "/" + alarm.getDate().get(Calendar.YEAR));
        location.setText(alarm.getDestiny());


        return customView;
    }
}
