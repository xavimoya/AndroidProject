package xavi.smartalarm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;

/**
 * Created by Xavi and Reylin on 13/03/2017.
 */

public class AddAlarm extends AppCompatActivity {

    private static final int RESULT_ADDALARM = 100;

    private static final int RESULT_LOCATION = 101;

    private TimePicker timePicker;
    private TextView title;
    private Button button_save, button_date, button_location;

    private DatePickerDialog datePickerDialog;
    private TimePickerFragment timePickerFragment;

    private int iday, imonth, iyear;
    private final Calendar c = Calendar.getInstance();

    private Double latitude,longitude;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addalarm);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iyear = c.get(Calendar.YEAR); // current year
        imonth = c.get(Calendar.MONTH); // current month (starts on 0)
        iday = c.get(Calendar.DAY_OF_MONTH); // current day

        /*Time*/
        if(Build.VERSION.SDK_INT < 23){
            timePickerFragment = new TimePickerFragment();
            Button b = (Button)findViewById(R.id.button_time);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerFragment.show(getSupportFragmentManager(), "timePicker");
                }
            });
         //   timePickerFragment.
        }else{
            timePicker = (TimePicker) findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
        }


        title = (EditText) findViewById(R.id.textView_title);
        button_save = (Button) findViewById(R.id.button_set);




        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if exist necessary data
                TextView tvDate = (TextView) findViewById(R.id.dateAlarm);
                EditText etTitle = (EditText) findViewById(R.id.textView_title);
                TextView tvLocation = (TextView)findViewById(R.id.locationAlarm);
                //check Date - check Title - check location (in process) - the time not necessary check, time now by default.
                if (tvDate.getText().toString().isEmpty() || etTitle.getText().toString().isEmpty() || tvLocation.toString().isEmpty()) {
                    Toast.makeText(AddAlarm.this, R.string.ToastMoreNecessaryData, Toast.LENGTH_SHORT).show();
                } else if(etTitle.getText().toString().contains("_")){
                    Toast.makeText(AddAlarm.this, R.string.ToastBadCharacter, Toast.LENGTH_LONG).show();
                } else
                {

                    Intent intent = new Intent();
                    if(Build.VERSION.SDK_INT > 23){
                        intent.putExtra(getString(R.string.hour), timePicker.getHour());
                        intent.putExtra(getString(R.string.minute), timePicker.getMinute());
                    }else{
                        intent.putExtra(getString(R.string.hour),timePickerFragment.gethour() );
                        intent.putExtra(getString(R.string.minute), timePickerFragment.getminute());
                    }

                    intent.putExtra(getString(R.string.titleUntranslatable), title.getText().toString());
                    intent.putExtra(getString(R.string.day), iday);
                    intent.putExtra(getString(R.string.month), imonth);
                    intent.putExtra(getString(R.string.year), iyear);
                    intent.putExtra(getString(R.string.location),tvLocation.getText().toString());
                    intent.putExtra(getString(R.string.latitude),latitude);
                    intent.putExtra(getString(R.string.longitude),longitude);
                    setResult(RESULT_ADDALARM, intent);
                    finish();
                }

            }
        });


        /*Date*/
        button_date = (Button) findViewById(R.id.button_date);
        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddAlarm.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                TextView tvDate = (TextView) findViewById(R.id.dateAlarm);
                                tvDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                                iday = dayOfMonth;
                                imonth = monthOfYear;
                                iyear = year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //Location
        button_location = (Button) findViewById(R.id.button_location);
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddAlarm.this, MapsActivity.class);
                startActivityForResult(i, RESULT_LOCATION);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == resultCode && resultCode == RESULT_LOCATION) {
            String location = data.getExtras().getString(getString(R.string.location));
            String street = data.getExtras().getString(getString(R.string.StreetUntranslatable));
            latitude = data.getExtras().getDouble(getString(R.string.latitude));
            longitude = data.getExtras().getDouble(getString(R.string.longitude));
            TextView tv = (TextView)findViewById(R.id.locationAlarm);
            if(location==null)location="";
            if(street==null)street="";
            tv.setText(location + ", " + street);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        TextView tvDate = (TextView) findViewById(R.id.dateAlarm);
        TextView tv = (TextView)findViewById(R.id.locationAlarm);
        state.putString(getString(R.string.DATE),tvDate.getText().toString());
        state.putString(getString(R.string.LOCATION),tv.getText().toString());

        state.putInt(getString(R.string.day), iday);
        state.putInt(getString(R.string.month), imonth);
        state.putInt(getString(R.string.year), iyear);
        if(latitude!=null) state.putDouble(getString(R.string.latitude),latitude);
        if(longitude!=null) state.putDouble(getString(R.string.longitude),longitude);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TextView tvDate = (TextView) findViewById(R.id.dateAlarm);
        TextView tv = (TextView)findViewById(R.id.locationAlarm);
        tvDate.setText(savedInstanceState.getString(getString(R.string.DATE)));
        tv.setText(savedInstanceState.getString(getString(R.string.LOCATION)));

        iday = savedInstanceState.getInt(getString(R.string.day));
        imonth = savedInstanceState.getInt(getString(R.string.month));
        iyear = savedInstanceState.getInt(getString(R.string.year));
        latitude = savedInstanceState.getDouble(getString(R.string.latitude));
        longitude = savedInstanceState.getDouble(getString(R.string.longitude));

    }

}
