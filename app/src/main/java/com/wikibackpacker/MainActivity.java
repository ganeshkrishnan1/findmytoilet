package com.wikibackpacker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tapreason.sdk.TapReason;
import com.wikibackpacker.utils.Constant;
import com.wikibackpacker.utils.GPSDetector;
import com.wikibackpacker.utils.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity
{

    public static String apiCampgrounds = Constant.HOSTNAME + "findAmenity/1";
    public static String apiHostels = Constant.HOSTNAME + "findAmenity/4";
    public static String apiDayUseArea = Constant.HOSTNAME + "findAmenity/8";
    public static String apiPointsOfnterest = Constant.HOSTNAME + "findAmenity/16";
    public static String apiInfoCenter = Constant.HOSTNAME + "findAmenity/32";
    public static String apiToilets = Constant.HOSTNAME + "findToilets";
    public static String apiShowers = Constant.HOSTNAME + "findToilets/2";
    public static String apiDrinkingWater = Constant.HOSTNAME + "findToilets/1";
    public static String apiCaravanParks = Constant.HOSTNAME + "findAmenity/2";
    public static String apiBBQSpots = Constant.HOSTNAME + "findBBQLocations";

    public static boolean bolLocationType = true;
    public static String currentLatitude = "";
    public static String currentLongitude = "";
    public static String cityName = "";

    String downloadedJSONString;
    boolean isDownloadedJSONData = false;
    ProgressBar myProgressBar;
    TextView txtStatus;
    CountDownTimer countDownTimer;
    GPSDetector gpsDetector;

    @Override
    protected void onStart()
    {
        super.onStart();
        TapReason.register( this );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        TapReason.unRegister( this );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        setContentView(R.layout.activity_main);
        if(null!= getSupportActionBar()) {
            getSupportActionBar().hide();
        }
        bolLocationType = true;

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "brown.ttf");
        txtStatus.setTypeface(customFont);
        myProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        myProgressBar.setMax(10);
        myProgressBar.setProgress(0);

        // Check File already have Cache data
        if (isFileCachedAvailable()) {
            try {
                myProgressBar.setVisibility(View.GONE);
                currentLatitude = PrefUtils.getPref(getApplicationContext(), PrefUtils.PRF_Latitude, "");
                currentLongitude = PrefUtils.getPref(getApplicationContext(), PrefUtils.PRF_Longitude, "");

                downloadedJSONString = readFromFile();
                isDownloadedJSONData = true;
                countDownTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        checkDownloadStatus();
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            Log.e("File","After File not Available");
            onGPSCheck();
        }
    }


    private void onGPSCheck() {
        gpsDetector = new GPSDetector(MainActivity.this);

        if (gpsDetector.canGetLocation()) {
            String latitude = String.valueOf(gpsDetector.getLatitude());
            String longitude = String.valueOf(gpsDetector.getLongitude());
//            Log.e("Location gd", "latitude " + latitude + " longitude " + longitude);

            if (!latitude.equals("0.0") && !longitude.equals("0.0")) {
                currentLatitude = latitude;
                currentLongitude = longitude;

                PrefUtils.setPref(getApplicationContext(),PrefUtils.PRF_Latitude,latitude);
                PrefUtils.setPref(getApplicationContext(),PrefUtils.PRF_Longitude,longitude);

                onLocationReceived();
                return;
            }
        }
        onGPSDialog();
    }



        private void onGPSDialog() {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("GPS unavailable, Please enable GPS from Settings then click Ok");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivity(intent);
                    onGPSDialog();
                }
            });

            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    txtStatus.setText(getString(R.string.GettingLocationMSG));
                    dialog.cancel();
                    CountDownTimer countDownTimer = new CountDownTimer(4000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            onGPSCheck();
                        }
                    }.start();
                }
            });
            alertDialog.show();
    }


    public void onLocationReceived()
    {

        txtStatus.setText(getString(R.string.LocationReceived));

        JSONDownloader jsonDownloader = new JSONDownloader();
        try {
            jsonDownloader.execute(getApiCampgrounds(), getApiHostels(), getApiDayUseArea(), getApiPointsOfnterest(), getApiInfoCenter(), getApiToilets(), getApiShowers(), getApiDrinkingWater(), getApiCaravanParks(), getApiBBQSpots());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkDownloadStatus()
    {
        if(isDownloadedJSONData)
        {
//            writeToFile(downloadedJSONString);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public void writeToFile(String data) {
        try {
            if (!data.equals("")) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("WikiBackPackerJSONData.txt", MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getApiCampgrounds() {
        if (bolLocationType) {
            return apiCampgrounds + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiCampgrounds + "?location=" + cityName;
        }
    }

    public static String getApiHostels() {
        if (bolLocationType) {
            return apiHostels + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiHostels + "?location=" + cityName;
        }
    }

    public static String getApiDayUseArea() {
        if (bolLocationType) {
            return apiDayUseArea + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiDayUseArea + "?location=" + cityName;
        }
    }

    public static String getApiPointsOfnterest() {
        if (bolLocationType) {
            return apiPointsOfnterest + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiPointsOfnterest + "?location=" + cityName;
        }
    }

    public static String getApiInfoCenter() {
        if (bolLocationType) {
            return apiInfoCenter + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiInfoCenter + "?location=" + cityName;
        }
    }

    public static String getApiToilets() {
        if (bolLocationType) {
            return apiToilets + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiToilets + "?location=" + cityName;
        }
    }

    public static String getApiShowers() {
        if (bolLocationType) {
            return apiShowers + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiShowers + "?location=" + cityName;
        }
    }

    public static String getApiCaravanParks() {
        if (bolLocationType) {
            return apiCaravanParks + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiCaravanParks + "?location=" + cityName;
        }
    }

    public static String getApiDrinkingWater() {
        if (bolLocationType) {
            return apiDrinkingWater + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiDrinkingWater + "?location=" + cityName;
        }
    }

    public static String getApiBBQSpots() {
        if (bolLocationType) {
            return apiBBQSpots + "/" + currentLatitude + "/" + currentLongitude;
        } else {
            return apiBBQSpots + "?location=" + cityName;
        }
    }
    public boolean isFileCachedAvailable() {
        try {
            InputStream inputStream = openFileInput("WikiBackPackerJSONData.txt");
            if(inputStream!=null)
            {
//                Log.e("File","Available");
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.e("File","Not Available");

        return false;
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
//                    Log.e("URL GET ", "POS " + i + " " + urls[i]);
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
//                return "Failed";
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {

            if (!s.equals("")) {
                writeToFile(s);

                downloadedJSONString = s;
//                downloadedJSONString = readFromFile();

            }
            isDownloadedJSONData = true;
            checkDownloadStatus();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            myProgressBar.setProgress(values[0]);
        }
    }

}
