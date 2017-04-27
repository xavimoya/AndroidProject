package xavi.smartalarm;

/*
 * Created by Xavi on 23/3/17.
 *
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


class WeatherPrevisionAPI extends AsyncTask<String, Void, String> {
    private final MainActivity MainActivity;
    private  Alarm alarm;

    // this constructor takes the activity as the parameter.
    // that way we can use the activity later to populate the weather value fields
    // on the screen


    WeatherPrevisionAPI(MainActivity MainActivity, Alarm alarm) {
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
                String s;

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

        try {
            // parse the json result returned from the service
            JSONObject jsonResult = new JSONObject(result);

            String weather = jsonResult.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");

            int weatherCode = jsonResult.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getInt("id");

            // more info in: https://openweathermap.org/weather-conditions
            if(weatherCode>=200 && weatherCode<300){ //Thunderstorm
                editAlarm(15,weather);
            }else if(weatherCode>=300 && weatherCode<400){ //Drizzle
                editAlarm(10,weather);
            }else if(weatherCode>=500 && weatherCode<600){ //Rain
                editAlarm(10,weather);
            }else if(weatherCode>=600 && weatherCode<700){ //Snow
                editAlarm(20,weather);
            }else if(weatherCode>=700 && weatherCode<800){ //Atmosphere
                editAlarm(10,weather);
            }else if(weatherCode>=800 && weatherCode<900){ //800 Clear, 80X Clouds
                editAlarm(2,weather);
            }else if(weatherCode>=900 && weatherCode<907){ //Extreme
                editAlarm(40,weather);
            }else if(weatherCode>=950 && weatherCode<1000){  //Additional
                if(weatherCode == 962) editAlarm(40,weather); //Hurricane
                else if(weatherCode == 961) editAlarm(35,weather); //violent storm
                else if(weatherCode == 960 || weatherCode == 959) editAlarm(25,weather); //storm or several gale
                else editAlarm(weatherCode-950,weather); // gale(958)...wind...breeze..calm(951)
            }else{//I don't know what happens..
                editAlarm(5,weather); // Alarm sounds 5 minutes before
            }

        } catch (JSONException e) {
            e.printStackTrace();
            editAlarm(2,"IDK");
        }

    }

    private void editAlarm(int x,String w){
        //The alarm sounds X minutes before

        int minute = alarm.getMinute();
        if(minute>(x-1)) alarm.setMinute(minute - x);
        else{
            int hour = alarm.getHour();
            alarm.setHour(hour-1);
            alarm.setMinute(60-(x-minute));
        }
        Toast.makeText(this.MainActivity, String.format(this.MainActivity.getResources().getString(R.string.ToastAlarmMeteo),w,x),Toast.LENGTH_SHORT).show();
        MainActivity.createAlarm(alarm);
    }

}