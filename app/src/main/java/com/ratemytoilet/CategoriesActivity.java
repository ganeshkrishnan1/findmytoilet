package com.ratemytoilet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ratemytoilet.adapter.NavDrawerListAdapter;
import com.ratemytoilet.adapter.SuggestionAdapter;
import com.ratemytoilet.utils.Constant;
import com.ratemytoilet.utils.GPSDetector;
import com.ratemytoilet.utils.NavDrawerItem;
import com.ratemytoilet.utils.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CategoriesActivity extends AppCompatActivity {

    PicAdapter imgAdapterSix;
    PicAdapter imgAdapterSeven;
    PicAdapter imgAdapterEight;
    String jsonString;

    List<HashMap<String, String>> listToilets;
    List<HashMap<String, String>> listShowers;
    List<HashMap<String, String>> listDrinkingWater;

    Typeface customFont;

    Button btnFindToilet;
    Button btnFindWater;
    Button btnFindShower;
    //-- load data again var
    ProgressDialog pDialog = null;
    //- Gallery current Image Name
    TextView txtToiletsName, txtShowersName, txtDrinkingWaterName;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    GPSDetector gpsDetector;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void onClickCategory(View view) {
        Intent intent = new Intent(getApplicationContext(), SingleCategoryListActivity.class);

        int id = view.getId();
        switch (id) {
            case R.id.txtToilets:
                intent.putExtra("singleCategoryDetails", (Serializable) listToilets);
                intent.putExtra("category", "toilets");
                break;
            case R.id.txtShowers:
                intent.putExtra("singleCategoryDetails", (Serializable) listShowers);
                intent.putExtra("category", "showers");
                break;
            case R.id.txtDrinkingWater:
                intent.putExtra("singleCategoryDetails", (Serializable) listDrinkingWater);
                intent.putExtra("category", "drinkingwater");
                break;
        }

        startActivity(intent);
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) {
            //  return;
        }
        setContentView(R.layout.activity_categories);

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color_500)));
        setTitle("Find My Toilet");

        customFont = Typeface.createFromAsset(getAssets(), "brown.ttf");

        jsonString = getIntent().getStringExtra("jsonString");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // list item in slider at  details
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));


        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        navMenuIcons.recycle();
        View headerView = getLayoutInflater().inflate(R.layout.nav_header_main, null, false);
        mDrawerList.addHeaderView(headerView, null, false);
        LinearLayout navHeader = (LinearLayout) headerView.findViewById(R.id.navHeader);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SingleCategoryListActivity.class);
                switch (position) {
                    case 1:
                        intent.putExtra("singleCategoryDetails", (Serializable) listToilets);
                        intent.putExtra("category", "toilets");
                        break;
                    case 2:
                        intent.putExtra("singleCategoryDetails", (Serializable) listShowers);
                        intent.putExtra("category", "showers");
                        break;
                    case 3:
                        intent.putExtra("singleCategoryDetails", (Serializable) listDrinkingWater);
                        intent.putExtra("category", "drinkingwater");
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);

                startActivity(intent);
            }
        });
/*
        setupDrawer();
*/

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;
        ImageView imgParallax = (ImageView) findViewById(R.id.imgParallax);
        imgParallax.setLayoutParams(new RelativeLayout.LayoutParams(deviceWidth, deviceHeight / 2));
        imgParallax.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load( "https://upload.wikimedia.org/wikipedia/en/thumb/c/c3/GovHack_logo.svg/1280px-GovHack_logo.svg.png").into(imgParallax);


        parseJSONString(jsonString);


        btnFindToilet = (Button) findViewById(R.id.btnFindToilet);
        btnFindWater = (Button) findViewById(R.id.btnFindWater);
        btnFindShower = (Button) findViewById(R.id.btnFindShower);

        btnFindToilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCategory(0);

            }
        });
        btnFindWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCategory(1);

            }
        });
        btnFindShower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCategory(2);

            }
        });

        TextView txtToilets = (TextView) findViewById(R.id.txtToilets);
        txtToilets.setTypeface(customFont);
        txtToilets.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtShowers = (TextView) findViewById(R.id.txtShowers);
        txtShowers.setTypeface(customFont);
        txtShowers.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtDrinkingWater = (TextView) findViewById(R.id.txtDrinkingWater);
        txtDrinkingWater.setTypeface(customFont);
        txtDrinkingWater.setTextColor(getResources().getColor(R.color.title_text_color));
        txtToiletsName = (TextView) findViewById(R.id.txtToiletsName);
        txtToiletsName.setTypeface(customFont);
        txtShowersName = (TextView) findViewById(R.id.txtShowersName);
        txtShowersName.setTypeface(customFont);
        txtDrinkingWaterName = (TextView) findViewById(R.id.txtDrinkingWaterName);
        txtDrinkingWaterName.setTypeface(customFont);
        loadGalleryData();

    }

    private void refreshLocationData() {

        MainActivity.bolLocationType = true;
        JSONDownloader jsonDownloader = new JSONDownloader();
        try {
            jsonDownloader.execute(MainActivity.getApiToilets(), MainActivity.getApiShowers(), MainActivity.getApiDrinkingWater());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCategory(int position) {
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;


        Intent subActivity = new Intent(getApplicationContext(),
                DetailsActivity.class);
        int orientation = getResources().getConfiguration().orientation;

        subActivity
                .putExtra("selectedIndex", 0)
                .putExtra("IMG_orientation", orientation).
                putExtra("IMG_left", 0).
                putExtra("IMG_top", 20).
                putExtra("IMG_width", deviceWidth).
                putExtra("IMG_height", deviceHeight *4/10);
        switch (position) {
            case 0:
                subActivity.putExtra("singleCategoryDetails", (Serializable) listToilets);
                subActivity.putExtra("category", "toilets");
                break;
            case 1:
                subActivity.putExtra("singleCategoryDetails", (Serializable) listShowers);
                subActivity.putExtra("category", "showers");
                break;
            case 2:
                subActivity.putExtra("singleCategoryDetails", (Serializable) listDrinkingWater);
                subActivity.putExtra("category", "drinkingwater");
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        startActivity(subActivity);
        overridePendingTransition(0, 0);
    }

    private void loadGalleryData() {
        Gallery gallerySix = (Gallery) findViewById(R.id.gallerySix);
        imgAdapterSix = new PicAdapter(this, listToilets, 6);
        gallerySix.setAdapter(imgAdapterSix);
        gallerySix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtToiletsName.setText("\n" + imgAdapterSix.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gallerySix.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pageTransform(view, listToilets, position, "toilets");

            }
        });

        Gallery gallerySeven = (Gallery) findViewById(R.id.gallerySeven);
        imgAdapterSeven = new PicAdapter(this, listShowers, 7);
        gallerySeven.setAdapter(imgAdapterSeven);
        gallerySeven.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtShowersName.setText("\n" + imgAdapterSeven.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gallerySeven.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pageTransform(view, listShowers, position, "showers");

            }
        });

        Gallery galleryEight = (Gallery) findViewById(R.id.galleryEight);
        imgAdapterEight = new PicAdapter(this, listDrinkingWater, 8);
        galleryEight.setAdapter(imgAdapterEight);
        galleryEight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtDrinkingWaterName.setText("\n" + imgAdapterEight.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        galleryEight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pageTransform(view, listDrinkingWater, position, "drinkingwater");

            }
        });


    }

    private void pageTransform(View view, List<HashMap<String, String>> mList, int mPosition, String strCat) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        Intent subActivity = new Intent(CategoriesActivity.this,
                DetailsActivity.class);
        int orientation = getResources().getConfiguration().orientation;
        subActivity.
                putExtra("singleCategoryDetails", (Serializable) mList).
                putExtra("selectedIndex", mPosition).
                putExtra("category", strCat).

                putExtra("IMG_orientation", orientation).
                putExtra("IMG_left", screenLocation[0]).
                putExtra("IMG_top", screenLocation[1]).
                putExtra("IMG_width", view.getWidth()).
                putExtra("IMG_height", view.getHeight());
        startActivity(subActivity);
        overridePendingTransition(0, 0);
    }

    public void parseJSONString(String jsonString) {

        listToilets = new ArrayList<>();
        listShowers = new ArrayList<>();
        listDrinkingWater = new ArrayList<>();

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();


            jsonArray = jsonRootObject.optJSONArray("toilets");
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=toilets");
                hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                listToilets.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("showers");
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=showers");
                hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                listShowers.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("drinkingwater");
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=drinkingwater");
                hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                listDrinkingWater.add(hm);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
//-- Load Data Again

    public void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("WikiBackPackerJSONData.txt", MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("WikiBackPackerJSONData.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public class JSONDownloader extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            OkHttpClient client = new OkHttpClient();
            try {
                for (int i = 0; i < urls.length; i++) {
                    String result = "";

                    Request request = new Request.Builder().url(urls[i]).build();
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                    writeToFile(result);
                    JSONArray jsonArray = new JSONArray(result);
                    switch (i) {
                        case 0:
                            jsonObject.put("toilets", jsonArray);
                            break;
                        case 1:
                            jsonObject.put("showers", jsonArray);
                            break;
                        case 2:
                            jsonObject.put("drinkingwater", jsonArray);
                            break;
                    }
                    publishProgress(i + 1);
                }
                return jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        public  void writeToFile(String data) {
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


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(CategoriesActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!s.equals("")) {
//-- Store data if data from current location
                    if (MainActivity.bolLocationType) {
                        writeToFile(s);
                    }
                    jsonString = s;
//                if (jsonString.equals("") || jsonString.equals("Failed")) {
//                    jsonString = readFromFile();
//                }
                    parseJSONString(jsonString);
                    loadGalleryData();
                } else {
                    jsonString = readFromFile();
                    parseJSONString(jsonString);
                    loadGalleryData();
                }
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    private void onGPSCheck() {
        gpsDetector = new GPSDetector(CategoriesActivity.this);

        if (gpsDetector.canGetLocation()) {
            String latitude = String.valueOf(gpsDetector.getLatitude());
            String longitude = String.valueOf(gpsDetector.getLongitude());
//            Log.e("Location New", "latitude " + latitude + " longitude " + longitude);

            if (!latitude.equals("0.0") && !longitude.equals("0.0")) {
                MainActivity.currentLatitude = latitude;
                MainActivity.currentLongitude = longitude;

                PrefUtils.setPref(getApplicationContext(), PrefUtils.PRF_Latitude, latitude);
                PrefUtils.setPref(getApplicationContext(), PrefUtils.PRF_Longitude, longitude);

                refreshLocationData();
                return;
            }
        } else {
            Toast.makeText(getApplicationContext(), "GPS not enabled", Toast.LENGTH_SHORT).show();
            return;
        }
        onGPSDialog();
    }

    private void onGPSDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("GPS unavailable, Please enable GPS from Settings then click Ok");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                CategoriesActivity.this.startActivity(intent);
                onGPSDialog();
            }
        });

        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            onGPSCheck();

        }

/*        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

}
