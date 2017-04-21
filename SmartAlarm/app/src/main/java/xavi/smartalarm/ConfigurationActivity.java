package xavi.smartalarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Xavi on 21/4/17.
 */


class ConfigurationActivity extends AppCompatActivity implements View.OnClickListener{


    private static SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);


    }

    @Override
    public void onClick(View v) {

    }
}
