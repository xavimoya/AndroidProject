package xavi.smartalarm;

/**
 * Created by Xavi on 23/3/17.
 */

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class WeatherPrevisionAPI extends AsyncTask<String, Void, String> {
    private final MainActivity MainActivity;
    private  Alarm alarm;

// this constructor takes the activity as the parameter.
// that way we can use the activity later to populate the weather value fields
// on the screen


    public WeatherPrevisionAPI(MainActivity MainActivity, Alarm alarm) {
        this.MainActivity = MainActivity;
        this.alarm = alarm;
    }

    @Override
    protected String doInBackground(String... urls) {

// this weather service method will be called after the service executes.
// it will run as a separate process, and will populate the activity in the onPostExecute
// method below

        String response = "";
// loop through the urls (there should only be one!) and call an http Get using the URL passed
// to this service
        System.out.println( "---------------\n"+urls[0]+ "\n ------------------");

        for (String url : urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {

// make the http request for the weather data
                HttpResponse execute = client.execute(httpGet);

// get the content of the result returned when the response comes back
// it should be a json object
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";

// populate the response string which will be passed later into the post execution
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }
    

    @Override
    protected void onPostExecute(String result) {

        System.out.println("AQUI -------------- + \n" + result + "\n ------------------");
        try {  //EXAMPLE

// parse the json result returned from the service
            JSONObject jsonResult = new JSONObject(result);

            double humidity = jsonResult.getJSONObject("main").getDouble("humidity");

// parse out the description from the JSON result
            String description = jsonResult.getJSONArray("weather").getJSONObject(0).getString("description");

            System.out.println("AQUI -------------- + humidity \n" + humidity + "\n ------------------ Description \n" + description + "\n ------------------");



        } catch (JSONException e) {
            e.printStackTrace();
        }


        editAlarm();


    }

    private void editAlarm(){
        //set alarm to 3:20 to test
        //alarm.setHour(3);
        //alarm.setMinute(20);
        int minute = alarm.getMinute();
        if(minute>4) alarm.setMinute(minute - 5);
        else{
            int hour = alarm.getHour();
            alarm.setHour(hour-1);
            alarm.setMinute(60-(5-minute));
        }
        MainActivity.createAlarm(alarm);
    }

}