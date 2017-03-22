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
 * Created by Reylin on 13/03/2017.
 */

public class AddAlarm extends AppCompatActivity {

    private static final int RESULT_ADDALARM = 100;


    private TimePicker timePicker;
    private TextView title;
    private Button button_save, button_date;

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    private int iday,imonth,iyear;
    private final Calendar c = Calendar.getInstance();
    


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addalarm);

        iyear= c.get(Calendar.YEAR); // current year
        imonth= c.get(Calendar.MONTH) +1; // current month (starts on 0)
        iday = c.get(Calendar.DAY_OF_MONTH); // current day

        /*Time*/
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        title = (EditText)findViewById(R.id.textView_title);
        button_save = (Button)findViewById(R.id.button_set);

        timePicker.setIs24HourView(true);


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if exist necessary data
                TextView tvDate = (TextView)findViewById(R.id.dateAlarm);
                EditText etTitle = (EditText)findViewById(R.id.textView_title);
                //check Date - check Title - check location (in process) - the time not necessary check, time now by default.
                if(tvDate.getText().toString().isEmpty() || etTitle.getText().toString().isEmpty()){
                    Toast.makeText(AddAlarm.this,"You don't choose all necessary data", Toast.LENGTH_SHORT).show();
                }else {

                    Intent intent = new Intent();
                    intent.putExtra("Hour", timePicker.getHour());
                    intent.putExtra("Minute", timePicker.getMinute());
                    intent.putExtra("Title", title.getText().toString());
                    intent.putExtra("Day", iday);
                    intent.putExtra("Month", imonth);
                    intent.putExtra("Year", iyear);
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

                                monthOfYear = monthOfYear +1;

                              TextView tvDate = (TextView) findViewById(R.id.dateAlarm);
                                tvDate.setText(dayOfMonth +"/"+monthOfYear +"/"+year);
                                iday = dayOfMonth;
                                imonth = monthOfYear;
                                iyear = year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        /*------------------------------------------------------------------------------*/
    }
}
