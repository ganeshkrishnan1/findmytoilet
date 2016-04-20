package com.wikibackpacker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.wikibackpacker.utils.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import io.fabric.sdk.android.Fabric;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements LocationListener {

    String apiCampgrounds = Constant.HOSTNAME+ "findAmenity/1";
    String apiHostels = Constant.HOSTNAME+"findAmenity/4";
    String apiDayUseArea = Constant.HOSTNAME+"findAmenity/8";
    String apiPointsOfnterest = Constant.HOSTNAME+"findAmenity/16";
    String apiInfoCenter = Constant.HOSTNAME+"findAmenity/32";
    String apiToilets =Constant.HOSTNAME+ "findToilets/";
    String apiShowers =Constant.HOSTNAME+ "findToilets/2";
    String apiDrinkingWater = Constant.HOSTNAME+"findToilets/1";
    String apiCaravanParks = Constant.HOSTNAME+"findAmenity/2";
    String apiBBQSpots = Constant.HOSTNAME+"findBBQLocations/";

    String downloadedJSONString;

    String jsonFromFile;

    boolean isDownloadedJSONData = false;

    Handler handler;
    Runnable runnable;

    ProgressBar myProgressBar;

    LocationManager locationManager;
    String provider;
    Location location;

    TextView txtStatus;

    @Override
    public void onLocationChanged(Location location) {
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        updateAPI(lat,lon);

        txtStatus.setText("Location Received");

        locationManager.removeUpdates(this);
        onLocationReceived();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class JSONDownloader extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            OkHttpClient client = new OkHttpClient();
            try {
                for (int i = 0; i < urls.length; i++) {
                    String result = "";
                    /*URL url = new URL(urls[i]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream in = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();
                    while (data != -1) {
                        char currChar = (char) data;
                        result += currChar;
                        data = reader.read();

                    }*/
                    Request request  = new Request.Builder().url(urls[i]).build();
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                    JSONArray jsonArray = new JSONArray(result);
                    switch (i)
                    {
                        case 0:
                            jsonObject.put("campgrounds",jsonArray);
                            break;
                        case 1:
                            jsonObject.put("hostels", jsonArray);
                            break;
                        case 2:
                            jsonObject.put("dayusearea", jsonArray);
                            break;
                        case 3:
                            jsonObject.put("pointsofinterest", jsonArray);
                            break;
                        case 4:
                            jsonObject.put("infocenter", jsonArray);
                            break;
                        case 5:
                            jsonObject.put("toilets", jsonArray);
                            break;
                        case 6:
                            jsonObject.put("showers", jsonArray);
                            break;
                        case 7:
                            jsonObject.put("drinkingwater", jsonArray);
                            break;
                        case 8:
                            jsonObject.put("caravanparks", jsonArray);
                            break;
                        case 9:
                            jsonObject.put("bbqspots", jsonArray);
                            break;
                    }
                    publishProgress(i + 1);
                }
                return jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {

            writeToFile(s);

            downloadedJSONString = s;
            if (downloadedJSONString.equals("") || downloadedJSONString.equals("Failed"))
            {
                downloadedJSONString = readFromFile();
            }
            isDownloadedJSONData = true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            myProgressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        txtStatus = (TextView)findViewById(R.id.txtStatus);
        Typeface customFont = Typeface.createFromAsset(getAssets(),"brown.ttf");
        txtStatus.setTypeface(customFont);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(),true);
       //BUG provider can be null
        if(null!=provider && provider.contains("gps"))
        {
            onGPSAvailable();
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS unavailable, Please enable GPS and click OK");
            builder.setCancelable(false);
            builder.setPositiveButton("OK",null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button btnPstv = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    btnPstv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isGPSEnabled())
                            {
                                alertDialog.dismiss();
                                onGPSAvailable();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"GPS not enabled",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            alertDialog.show();
        }
    }

    public boolean isGPSEnabled()
    {
        provider = locationManager.getBestProvider(new Criteria(),true);
        if(null!=provider && provider.contains("gps"))
        {
            return true;
        }
        else
        {
            return  false;
        }
    }

    public void onGPSAvailable()
    {
        locationManager.requestLocationUpdates(provider, 400, 0.1f, this);
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            txtStatus.setText("Location Received");

            String lat = String.valueOf(location.getLatitude());
            String lon = String.valueOf(location.getLongitude());
            updateAPI(lat, lon);

            locationManager.removeUpdates(this);
            onLocationReceived();
        } else {
            txtStatus.setText("Getting Location, Please Wait");
        }
    }

    public void onLocationReceived()
    {
        myProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        myProgressBar.setMax(10);
        myProgressBar.setProgress(0);

        JSONDownloader jsonDownloader = new JSONDownloader();
        try {
            jsonDownloader.execute(apiCampgrounds, apiHostels, apiDayUseArea, apiPointsOfnterest, apiInfoCenter, apiToilets, apiShowers, apiDrinkingWater, apiCaravanParks, apiBBQSpots);
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                checkDownloadStatus();
                if (!isDownloadedJSONData) {
                    handler.postDelayed(this, 1000);
                } else {
                    handler.removeCallbacks(this);
                }
            }
        };

        handler.post(runnable);
    }

    public void checkDownloadStatus()
    {
        if(isDownloadedJSONData)
        {
            writeToFile(downloadedJSONString);
            Intent intent = new Intent(getApplicationContext(),CategoriesActivity.class);
            intent.putExtra("jsonString",downloadedJSONString);
            MainActivity.this.finish();
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("WikiBackPackerJSONData.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("WikiBackPackerJSONData.txt", MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAPI(String lat, String lon)
    {
        apiCampgrounds += "/" + lat + "/" + lon;
        apiHostels += "/" + lat + "/" + lon;
        apiDayUseArea += "/" + lat + "/" + lon;
        apiPointsOfnterest += "/" + lat + "/" + lon;
        apiInfoCenter += "/" + lat + "/" + lon;
        apiToilets += "/" + lat + "/" + lon;
        apiShowers += "/" + lat + "/" + lon;
        apiDrinkingWater += "/" + lat + "/" + lon;
        apiCaravanParks += "/" + lat + "/" + lon;
        apiBBQSpots += "/" + lat + "/" + lon;
    }
}
