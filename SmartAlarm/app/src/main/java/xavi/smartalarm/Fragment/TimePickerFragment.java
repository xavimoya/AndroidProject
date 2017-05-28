package xavi.smartalarm.Fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

import xavi.smartalarm.Activities.AddAlarm;

/*
 * Created by Xavi on 23/4/17.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int hour,minute;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.minute = minute;
        this.hour = hourOfDay;
        String text="";
        if(hourOfDay<10) text="0";
        text = text + hourOfDay + ":";
        if (minute<10) text = text + "0";
        text = text + minute;
        AddAlarm.tvTime.setText(text);
    }

    public int gethour(){
        return hour;
    }

    public int getminute(){
        return minute;
    }
}
