package xavi.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Calendar;

/**
 * Created by Reylin on 21/03/2017.
 */

public class AlarmList extends AppCompatActivity{

    ListView listView;
    Alarm[] alarms;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmlist_layout);

        listView = (ListView) findViewById(R.id.listView_alarms);

         /*Creacion de alarmas prueba*/

        Alarm alarm1,alarm2;
        Calendar date = Calendar.getInstance();
        /*year, month, day, hour, minute*/
        date.set(2017, 04, 15, getIntent().getIntExtra("Hour",0), getIntent().getIntExtra("Minute",0));
        alarm1 = new Alarm(date,getIntent().getStringExtra("Title"), "Barcelona");
        date.set(2017, 8, 6, getIntent().getIntExtra("Hour",0), getIntent().getIntExtra("Minute",0));
        alarm2 = new Alarm(date, getIntent().getStringExtra("Title"), "Lleida");
        //getIntent().getExtras().getString()

        alarms = new Alarm[]{alarm1,alarm2};
        ListAdapter adapterAlarm = new CustomAlarmAdapter(this, alarms);

        listView.setAdapter(adapterAlarm);
    }

    //Agregar los menus.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_addalarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addButton:
                Intent intent = new Intent(AlarmList.this, AddAlarm.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
