package xavi.smartalarm;

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
import java.util.StringTokenizer;

/**
 * Created by Xavi on 9/5/17.
 */

public class TrafficTimeAPI extends AsyncTask<String, Void, String> {
    private final MainActivity MainActivity;
    private  Alarm alarm;



    TrafficTimeAPI(MainActivity MainActivity, Alarm alarm) {
        this.MainActivity = MainActivity;
        this.alarm = alarm;
    }

    @Override
    protected String doInBackground(String... urls) {


        String response = "";
        // loop through the urls (there should only be one!) and call an http Get using the URL passed
        // to this service
        System.out.println( "---------------\n"+urls[0]+ "\n ------------------");

        for (String url : urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {

                // make the http request for the data
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

            String status = jsonResult.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getString("status");
            if(status.equals("OK")){

                String time = jsonResult.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration")
                        .getString("text");

                int h=0;
                if(time.contains("hours")){
                    StringTokenizer token = new StringTokenizer(time,"hours ");
                    h = Integer.parseInt(token.nextToken());
                }


                if(h<=1){
                    editAlarm(5);
                }else if(h<3){
                    editAlarm(10);
                }else if(h<4){
                    editAlarm(15);
                }else if(h<5){
                    editAlarm(20);
                }else if(h<6){
                    editAlarm(25);
                }else if(h<7){
                    editAlarm(30);
                }else if(h<8){
                    editAlarm(35);
                }else{
                    editAlarm(40);
                }

            }else{
                //no result
                Toast.makeText(this.MainActivity, String.format(this.MainActivity.getString(R.string.ToastAlarmNoTraffic),status),Toast.LENGTH_SHORT).show();
                MainActivity.createAlarm(alarm);

            }

            // more info in: https://developers.google.com/maps/documentation/distance-matrix/intro#DistanceMatrixRequests

        } catch (JSONException e) {
            e.printStackTrace();
            editAlarm(2);
        }

    }

    private void editAlarm(int x){
        //The alarm sounds X minutes before

        int minute = alarm.getMinute();
        if(minute>(x-1)) alarm.setMinute(minute - x);
        else{
            int hour = alarm.getHour();
            alarm.setHour(hour-1);
            alarm.setMinute(60-(x-minute));
        }
        Toast.makeText(this.MainActivity, String.format(this.MainActivity.getResources().getString(R.string.ToastAlarmTraffic),x),Toast.LENGTH_SHORT).show();
        MainActivity.createAlarm(alarm);
    }

}