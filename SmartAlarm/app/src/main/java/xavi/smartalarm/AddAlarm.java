package xavi.smartalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Reylin on 21/03/2017.
 */

public class AddAlarm extends Activity {

    TimePicker timePicker;
    TextView title;
    Button button_set;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addalarm);

        timePicker = (TimePicker)findViewById(R.id.timePicker);
        title = (EditText)findViewById(R.id.textView_title);
        button_set = (Button)findViewById(R.id.button_set);

        timePicker.setIs24HourView(true);

        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("Hour", timePicker.getHour());
                intent.putExtra("Minute", timePicker.getMinute());
                intent.putExtra("Title", title.getText().toString());
                startActivity(intent);
                Toast.makeText(getBaseContext(), title.getText().toString() + timePicker.getHour() + " : " + timePicker.getMinute(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
