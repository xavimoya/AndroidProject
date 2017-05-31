package xavi.smartalarm.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;

import xavi.smartalarm.R;

/*
 * Created by Xavi on 21/4/17.
 */


public class ConfigurationFragment extends PreferenceFragment{


    private static SharedPreferences sharedPreferences;
    Switch sw1,sw2,sw3;
    RadioButton rbF, rbH;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //add xml
        addPreferencesFromResource(R.xml.preferences);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //onSharedPreferenceChanged(sharedPreferences, getString(R.string.movies_categories_key));
    }



/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        preferences = getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE);


        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         sw1 = (Switch)findViewById(R.id.sw1);
         sw2 = (Switch)findViewById(R.id.sw2);
         sw3 = (Switch)findViewById(R.id.sw3);
        rbF = (RadioButton)findViewById(R.id.rbFirebase);
        rbH = (RadioButton)findViewById(R.id.rbHeroku);

        Button btnsave = (Button) findViewById(R.id.saveButton);
        btnsave.setOnClickListener(this);
        if (preferences.contains(getString(R.string.onlyWifi))){
            if(preferences.getString(getString(R.string.onlyWifi),getString(R.string.yes)).equals(getString(R.string.yes))){
                sw1.setChecked(true);
            }
        }
        if (preferences.contains(getString(R.string.useWeather))){
            if(preferences.getString(getString(R.string.useWeather),getString(R.string.yes)).equals(getString(R.string.yes))){
                sw2.setChecked(true);
            }
        }
        if (preferences.contains(getString(R.string.useTraffic))){
            if(preferences.getString(getString(R.string.useTraffic),getString(R.string.yes)).equals(getString(R.string.yes))){
                sw3.setChecked(true);
            }
        }
        if (preferences.contains(getString(R.string.useFirebase))){
            if(preferences.getString(getString(R.string.useFirebase),getString(R.string.yes)).equals(getString(R.string.yes))){
                rbF.setChecked(true);
            }else{
                rbH.setChecked(true);
            }
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveButton:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (sw1.isChecked()) editor.putString(getString(R.string.onlyWifi),getString(R.string.yes));
                else editor.putString(getString(R.string.onlyWifi),getString(R.string.no));
                if (sw2.isChecked()) editor.putString(getString(R.string.useWeather),getString(R.string.yes));
                else editor.putString(getString(R.string.useWeather),getString(R.string.no));
                if (sw3.isChecked()) editor.putString(getString(R.string.useTraffic),getString(R.string.yes));
                else editor.putString(getString(R.string.useTraffic),getString(R.string.no));
                if (rbF.isChecked()) editor.putString(getString(R.string.useFirebase),getString(R.string.yes));
                else editor.putString(getString(R.string.useFirebase),getString(R.string.no));

                editor.apply();

                finish();
                break;


        }

    }*/

}
