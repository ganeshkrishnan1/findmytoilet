package com.miracitechnology.wikibackpacker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

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


public class MainActivity extends AppCompatActivity {

    String apiCampgrounds = "http://api.wikibackpacker.com/api/findAmenity/1";
    String apiHostels = "http://api.wikibackpacker.com/api/findAmenity/4";
    String apiDayUseArea = "http://api.wikibackpacker.com/api/findAmenity/8";
    String apiPointsOfnterest = "http://api.wikibackpacker.com/api/findAmenity/16";
    String apiInfoCenter = "http://api.wikibackpacker.com/api/findAmenity/32";
    String apiToilets = "http://api.wikibackpacker.com/api/findToilets/";
    String apiShowers = "http://api.wikibackpacker.com/api/findToilets/2";
    String apiDrinkingWater = "http://api.wikibackpacker.com/api/findToilets/1";
    String apiCaravanParks = "http://api.wikibackpacker.com/api/findAmenity/2";
    String apiBBQSpots = "http://api.wikibackpacker.com/api/findBBQLocations/";

    String downloadedJSONString;

    String jsonFromFile;

    boolean isDownloadedJSONData = false;

    Handler handler;
    Runnable runnable;

    ProgressBar myProgressBar;

    public class JSONDownloader extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            try {
                for (int i = 0; i < urls.length; i++) {
                    String result = "";
                    URL url = new URL(urls[i]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream in = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();
                    while (data != -1) {
                        char currChar = (char) data;
                        result += currChar;
                        data = reader.read();

                    }
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
            downloadedJSONString = s;
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
        setContentView(R.layout.activity_main);

        myProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        myProgressBar.setMax(10);
        myProgressBar.setProgress(0);

        jsonFromFile = readFromFile();

        if (jsonFromFile.equals("") || jsonFromFile.equals("Failed"))
        {
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
        else
        {
            new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    myProgressBar.setProgress(myProgressBar.getProgress() + 3);
                }

                @Override
                public void onFinish() {
                    myProgressBar.setProgress(10);
                    Intent intent = new Intent(getApplicationContext(),CategoriesActivity.class);
                    intent.putExtra("jsonString",jsonFromFile);
                    MainActivity.this.finish();
                    startActivity(intent);
                }
            }.start();
        }
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
}
