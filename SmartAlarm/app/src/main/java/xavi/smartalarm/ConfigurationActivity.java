package xavi.smartalarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

/**
 * Created by Xavi on 21/4/17.
 */


class ConfigurationActivity extends AppCompatActivity implements View.OnClickListener{


    private static SharedPreferences preferences;
    Switch sw1,sw2,sw3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);


        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         sw1 = (Switch)findViewById(R.id.sw1);
         sw2 = (Switch)findViewById(R.id.sw2);
         sw3 = (Switch)findViewById(R.id.sw3);
        Button btnsave = (Button) findViewById(R.id.saveButton);
        btnsave.setOnClickListener(this);
        if (preferences.contains("onlyWifi")){
            if(preferences.getString("onlyWifi","yes").equals("yes")){
                sw1.setChecked(true);
            }
        }
        if (preferences.contains("useWeather")){
            if(preferences.getString("useWeather","yes").equals("yes")){
                sw2.setChecked(true);
            }
        }
        if (preferences.contains("useTraffic")){
            if(preferences.getString("useTraffic","yes").equals("yes")){
                sw3.setChecked(true);
            }
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveButton:
                SharedPreferences.Editor editor = preferences.edit();
                if (sw1.isChecked()) editor.putString("onlyWifi","yes");
                else editor.putString("onlyWifi","no");
                if (sw2.isChecked()) editor.putString("useWeather","yes");
                else editor.putString("useWeather","no");
                if (sw3.isChecked()) editor.putString("useTraffic","yes");
                else editor.putString("useTraffic","no");

                editor.apply();

                finish();
                break;


        }

    }
}
