package xavi.smartalarm.Activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import xavi.smartalarm.Fragment.ConfigurationFragment;

/**
 * Created by Xavi on 31/5/17.
 */

public class ConfigurationActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ConfigurationFragment()).commit();
    }
}
