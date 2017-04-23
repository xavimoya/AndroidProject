package xavi.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * Created by Xavi and Reylin on 4/3/17.
 *
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, ValueEventListener{

    private GoogleApiClient mGoogleApiClient;
    private final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private ProgressDialog mProgressDialog;
    private ImageView imageView;
    private TextView name, email;
    private DrawerLayout drawer;
    private Menu menu;
    private Drawable imageD;
    private String userID=null;
    private static final int RESULT_ADDALARM = 100;
    private SharedPreferences preferences;
    ListView listView;
    ArrayList<Alarm> alarms;
    ArrayAdapter<Alarm> adapterAlarm;
    AlarmManager alarmManager;
    private MenuItem i,i2;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Intent intent;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout);


        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu24);

        preferences = getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE);

        alarms = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView_alarms);
        adapterAlarm = new CustomAlarmAdapter(this, alarms);
        listView.setAdapter(adapterAlarm);
        listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                listView.refreshDrawableState();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });


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
                .requestIdToken(getString(R.string.idTokenGoogle))
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


        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, getString(R.string.LogAuthSignedIn) + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, getString(R.string.LogAuthSignedOut));
                }
                // ...
            }
        };

        database = FirebaseDatabase.getInstance();



        intent = new Intent(MainActivity.this, AlarmNotification.class);


    }



    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);


        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, getString(R.string.LogCachedSignIn));
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
    public void onRestart(){

        super.onRestart();


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        //databaseReference.removeEventListener(listener);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, getString(R.string.LogConnFail) + connectionResult);
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
        //sign in google
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(false);
                    }
                });
        mAuth.signOut();

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

            date.set(data.getIntExtra(getString(R.string.year),0),
                    data.getIntExtra(getString(R.string.month),0),
                    data.getIntExtra(getString(R.string.day),0),
                    data.getIntExtra(getString(R.string.hour),0),
                    data.getIntExtra(getString(R.string.minute),0),
                    0);  //seconds

            Alarm alarm = new Alarm(date,data.getExtras().getString(getString(R.string.title)), data.getExtras().getString(getString(R.string.location)));

            //not add yet
           // alarms.add(alarm);

           //Task to check weather
                if(preferences.getString(getString(R.string.useWeather),getString(R.string.yes)).equals(getString(R.string.yes))){
                    WeatherPrevisionAPI wpa = new WeatherPrevisionAPI(this, alarm);

                    Double latitude = data.getExtras().getDouble(getString(R.string.latitude));
                    Double longitude = data.getExtras().getDouble(getString(R.string.longitude));
                    String url = String.format(getResources().getString(R.string.urlAPIweather),latitude,longitude);

                    wpa.execute(url+getString(R.string.weatherAPIkey)); //URL of api + location selected

                    Toast.makeText(this, R.string.ToastAlarmMeteo,Toast.LENGTH_SHORT).show();

                }else{
                    createAlarm(alarm);
                }
            //Falta un IF per al transit

            //Tendriamos que usar el switch
            //Makes alarms sound
            startAlarm(true, alarm); //Valor del switch


        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, getString(R.string.LogHandleSignIn) + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            assert acct != null;
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, getString(R.string.LogSigninCredentialCompl) + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w(TAG, getString(R.string.LogSignInCredentials), task.getException());
                                Toast.makeText(MainActivity.this, R.string.ToastAuthFail,
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.topicNews));

                            }
                        }
                    });

            userID = acct.getId();
            name.setText(acct.getDisplayName());
            email.setText(acct.getEmail());
            //imageView.setImageURI(acct.getPhotoUrl());
            DownloadIMG Dimg = new DownloadIMG();
            Dimg.execute(acct.getPhotoUrl().toString());

            //Get alarms stored in database
            restoreAlarmsOfDatabase();


            updateUI(true);
        } else {
            userID = "null";
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    /**
     * This method is called when the user is signed in or sign out
     *
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
            if(i!=null)i.setVisible(true);

        } else { // signed out

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_layout).setVisibility(View.GONE);
            item.setTitle(getString(R.string.sign_in));
            item.setIcon(android.R.drawable.ic_media_play);
            name.setText(R.string.name_notSigned);
            email.setText("");
            imageView.setImageDrawable(imageD);
            menu.setGroupVisible(R.id.groupAlarmMenu,false);
            if(i!=null)i.setVisible(false);

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
            startActivity(new Intent(getApplicationContext(), ConfigurationActivity.class));

        }else if(id == R.id.deleteAlarms){
            removeAlarmsOfDatabase();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //Database Methods
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        // This method is called once with the initial value and again
        // whenever data at this location is updated.
       // String value = dataSnapshot.getValue(String.class);
       // Log.d(TAG, "Value is: " + value);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Failed to read value
        Log.w(TAG, getString(R.string.LogFailRead), databaseError.toException());
    }

    private class DownloadIMG extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String path = params[0];
            return DownloadImageFromPath(path);

        }

        private Bitmap DownloadImageFromPath(String path) {
            InputStream in;
            Bitmap bmp = null;
            int responseCode;
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
                Log.e(getString(R.string.exception), ex.toString());
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
        i = menu.findItem(R.id.addButton);
        i2 = menu.findItem(R.id.pauseButton);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addButton:
                newAlarm();
                break;
            case R.id.pauseButton:
                intent.putExtra(getString(R.string.info),getString(R.string.stop));
                sendBroadcast(intent);
                if(i2!=null)i2.setVisible(false);
                break;
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void newAlarm() {
        Intent intentTime = new Intent(MainActivity.this, AddAlarm.class);
        startActivityForResult(intentTime,RESULT_ADDALARM);
    }

    private void startAlarm(boolean state, Alarm alarm) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       // Intent intent;
        PendingIntent pendingIntent;

        //Represents the switch is true, this indicates that the alarm is activated
        if (state) { //true
           // intent = new Intent(MainActivity.this, AlarmNotification.class);
            intent.putExtra(getString(R.string.info),getString(R.string.sound));
            intent.putExtra(getString(R.string.text),alarm.getTitle());
            pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);


//            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);  //sounds now

            manager.set(AlarmManager.RTC_WAKEUP, alarm.getDate().getTimeInMillis(), pendingIntent);       //sounds when the alarm is configured

            if(i2!=null)i2.setVisible(true);

        }
    }



    public void createAlarm(Alarm alarm){

        //Add alarm after check weather prevision
        alarms.add(alarm);

        //Add alarm to database

        //myRef = database.getReference("User_" + userID);
        DatabaseReference username = myRef.child(getString(R.string.UserName));
        username.setValue(name.getText());
        DatabaseReference counter = myRef.child(getString(R.string.AlarmsCounterUntranslatable));
        counter.setValue(alarms.size());
        DatabaseReference alarmRef = myRef.child(getString(R.string.Alarm_Untranslatable)+  alarm.getHashCode());
        DatabaseReference title = alarmRef.child(getString(R.string.titleUntranslatable));
        title.setValue(alarm.getTitle());
        DatabaseReference date = alarmRef.child(getString(R.string.dateUntranslatable));
        date.setValue(alarm.getDate().getTime());
        //date.setValue(alarm.getDate().getTime().toString());
        DatabaseReference destiny = alarmRef.child(getString(R.string.destinyUntranslatable));
        destiny.setValue(alarm.getDestiny());

        // Read from the database
        myRef.addValueEventListener(this);

        adapterAlarm.notifyDataSetChanged(); //Notify that the listview has changes
    }

    private void restoreAlarmsOfDatabase() {
        //Get alarms from database

       // DatabaseReference alarmRef = database.getReference("User_" + userID);
        myRef = database.getReference(getString(R.string.User_Untranslatable) + userID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    if(snapshot.getKey().contains(getString(R.string.Alarm_Untranslatable))){
                        String name = snapshot.getKey();
                        name = name.substring(6);
                        boolean validAlarm = true;
                        for (Alarm a : alarms){
                            if (a.getHashCode() == Integer.parseInt(name)){
                                validAlarm = false;
                            }
                        }
                        if(validAlarm){
                            myRef.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Calendar cal = Calendar.getInstance();
                                    String title="";
                                    String dest = "";

                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                        switch (snapshot1.getKey()){
                                            case "Date":
                                                cal.setTime(snapshot1.getValue(Date.class));
                                                break;
                                            case "Title":
                                                title = (String) snapshot1.getValue();
                                                break;
                                            case "Destiny":
                                                dest = (String) snapshot1.getValue();
                                                break;
                                        }
                                    }
                                    Alarm a = new Alarm(cal,title,dest);
                                    a.setHashcode(Integer.parseInt(snapshot.getKey().substring(6)));
                                   // if (!alarms.contains(a)){
                                    if (alarmNotInAlarms(a)){
                                        alarms.add(a);
                                        adapterAlarm.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }


                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //


    }

    private boolean alarmNotInAlarms(Alarm a) {
        for(Alarm alarm : alarms){
            if (alarm.getHashCode() == a.getHashCode()) return false;

        }
        return true;
    }


    private void removeAlarmsOfDatabase(){
        //For all references
        DatabaseReference alarmRef = database.getReference(getString(R.string.User_Untranslatable) + userID);
        alarmRef.removeValue();
        alarms.clear();
        adapterAlarm.notifyDataSetChanged(); //Notify changes
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("USER",userID);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userID = savedInstanceState.getString("USER");
        restoreAlarmsOfDatabase();
    }

    private void setAlarm(Alarm alarm){

    }


}
