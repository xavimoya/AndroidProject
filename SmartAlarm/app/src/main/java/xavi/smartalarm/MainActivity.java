package xavi.smartalarm;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Xavi and Reylin on 4/3/17.
 *
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    private GoogleApiClient mGoogleApiClient;
    private final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private ProgressDialog mProgressDialog;
    private ImageView imageView;
    private TextView name, email;
    private DrawerLayout drawer;
    private Menu menu;
    private Drawable imageD;
    private static final int RESULT_ADDALARM = 100;
    ListView listView;
    ArrayList<Alarm> alarms;
    ArrayAdapter<Alarm> adapterAlarm;
    AlarmManager alarmManager;
    Switch alarmSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        setContentView(R.layout.drawerlayout);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alarms = new ArrayList<Alarm>();
        listView = (ListView) findViewById(R.id.listView_alarms);
        adapterAlarm = new CustomAlarmAdapter(this, alarms);
        listView.setAdapter(adapterAlarm);


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get Menu
        menu = navigationView.getMenu();


        //register the authentication button
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        //get the items of navigationView
        imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.head_imageView);
        name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.head_name);
        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.head_email);
        imageD = imageView.getDrawable();



        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Customize the SignIn button
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);



    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        //Result returned of create alarm
        else if(requestCode == resultCode && resultCode == RESULT_ADDALARM){
            //Result of AddAlarm
            //data is the new alarm configured
            Calendar date = Calendar.getInstance();

            //year, month, day, hour, minute

            date.set(data.getIntExtra("Year",0),
                    data.getIntExtra("Month",0),
                    data.getIntExtra("Day",0),
                    data.getIntExtra("Hour",0),
                    data.getIntExtra("Minute",0));

            Alarm alarm = new Alarm(date,data.getExtras().getString("Title"), "Barcelona");

            alarms.add(alarm);

            adapterAlarm.notifyDataSetChanged(); //Notify that the listview has another element
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            name.setText(acct.getDisplayName());
            email.setText(acct.getEmail());
            //imageView.setImageURI(acct.getPhotoUrl());
            DownloadIMG Dimg = new DownloadIMG();
            Dimg.execute(acct.getPhotoUrl().toString());
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    /**
     * This method is called when the user is signed in correctly.
     * Start the next activity.
     */

    private void updateUI(boolean signedIn) {
        MenuItem item =  menu.findItem(R.id.nav_signout);

        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_layout).setVisibility(View.VISIBLE);
            item.setTitle(getString(R.string.sign_out));
            item.setIcon(android.R.drawable.ic_menu_revert);
            menu.setGroupVisible(R.id.groupAlarmMenu,true);

        } else { // signed out

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_layout).setVisibility(View.GONE);
            item.setTitle(getString(R.string.sign_in));
            item.setIcon(android.R.drawable.ic_media_play);
            name.setText(R.string.name_notSigned);
            email.setText("");
            imageView.setImageDrawable(imageD);
            menu.setGroupVisible(R.id.groupAlarmMenu,false);

        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Newalarm) {
            newAlarm();
        } else if(id == R.id.nav_Editalarm){

        } else if (id == R.id.nav_signout) {
            if(item.getTitle() == getString(R.string.sign_out)) signOut();
            else if(item.getTitle() == getString(R.string.sign_in)) signIn();
        } else if (id == R.id.nav_manage) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;    }

    @Override
    public void onRestart(){
        super.onRestart();


    }

    private class DownloadIMG extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String path = params[0];
            return DownloadImageFromPath(path);

        }

        public Bitmap DownloadImageFromPath(String path) {
            InputStream in = null;
            Bitmap bmp = null;
            int responseCode = -1;
            try {

                URL url = new URL(path);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.connect();
                responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //download
                    in = con.getInputStream();
                    bmp = BitmapFactory.decodeStream(in);
                    in.close();
                }

            } catch (Exception ex) {
                Log.e("Exception", ex.toString());
            }
            return bmp;
        }

        @Override
        public void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

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
                newAlarm();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newAlarm() {
        Intent intentTime = new Intent(MainActivity.this, AddAlarm.class);
        intentTime.addFlags(intentTime.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intentTime,RESULT_ADDALARM);
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setAlarm(Alarm alarm){

    }
}
