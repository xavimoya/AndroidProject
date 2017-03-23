package xavi.smartalarm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private int iday, imonth, iyear;
    private final Calendar c = Calendar.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addalarm);

        iyear = c.get(Calendar.YEAR); // current year
        imonth = c.get(Calendar.MONTH) + 1; // current month (starts on 0)
        iday = c.get(Calendar.DAY_OF_MONTH); // current day

        /*Time*/
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        title = (EditText) findViewById(R.id.textView_title);
        button_save = (Button) findViewById(R.id.button_set);

        timePicker.setIs24HourView(true);


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if exist necessary data
                TextView tvDate = (TextView) findViewById(R.id.dateAlarm);
                EditText etTitle = (EditText) findViewById(R.id.textView_title);
                TextView tvLocation = (TextView)findViewById(R.id.locationAlarm);
                //check Date - check Title - check location (in process) - the time not necessary check, time now by default.
                if (tvDate.getText().toString().isEmpty() || etTitle.getText().toString().isEmpty() || tvLocation.toString().isEmpty()) {
                    Toast.makeText(AddAlarm.this, "You don't choose all necessary data", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent();
                    intent.putExtra("Hour", timePicker.getHour());
                    intent.putExtra("Minute", timePicker.getMinute());
                    intent.putExtra("Title", title.getText().toString());
                    intent.putExtra("Day", iday);
                    intent.putExtra("Month", imonth);
                    intent.putExtra("Year", iyear);
                    intent.putExtra("Location",tvLocation.getText().toString());
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

                                monthOfYear = monthOfYear + 1;

                                TextView tvDate = (TextView) findViewById(R.id.dateAlarm);
                                tvDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
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
            String location = data.getExtras().getString("Location");
            String street = data.getExtras().getString("Street");
            TextView tv = (TextView)findViewById(R.id.locationAlarm);
            if(location==null)location="";
            if(street==null)street="";
            tv.setText(location + ", " + street);
        }
    }
}
