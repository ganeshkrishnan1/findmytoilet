package com.wikibackpacker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wikibackpacker.utils.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SingleCategoryActivity extends FragmentActivity {
    public static int intCatPOS = 0;
    static String latTmp = "";
    static String lonTmp = "";
    private static String[] allCatURL = {
            MainActivity.apiCampgrounds,
            MainActivity.apiHostels,
            MainActivity.apiDayUseArea,
            MainActivity.apiPointsOfnterest,
            MainActivity.apiInfoCenter,
            MainActivity.apiToilets,
            MainActivity.apiShowers,
            MainActivity.apiDrinkingWater,
            MainActivity.apiCaravanParks,
            MainActivity.apiBBQSpots
    };
    List<HashMap<String, String>> singleCategoryDetails;
    String category;
    LatLngBounds bounds;
    List<Marker> markers;
    Gallery gallery;
    Toolbar mToolbar;
    Typeface customFont;
    Geocoder geocoder;
    ProgressBar progressBar;
    TextView txtResetMap;
    PicAdapter picAdapter;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public void resetMap(View view) {
        progressBar.setVisibility(View.VISIBLE);
        txtResetMap.setVisibility(View.GONE);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        getApiCampgrounds();

    }

    private int getCat(String mCategory) {

        switch (mCategory) {
            case "campgrounds":
                return 0;
            case "hostels":
                return 1;
            case "dayusearea":
                return 2;
            case "pois":
                return 3;
            case "infocenter":
                return 4;
            case "toilets":
                return 5;
            case "showers":
                return 6;
            case "drinkingwater":
                return 7;
            case "caravanparks":
                return 8;
            case "bbq":
                return 9;
        }
        return 0;
    }

    private void getApiCampgrounds() {
        JSONDownloader jsonDownloader = new JSONDownloader();
        try {
            String strURL = allCatURL[intCatPOS] + "/" + latTmp + "/" + lonTmp;
            jsonDownloader.execute(strURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category);

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtResetMap = (TextView) findViewById(R.id.txtResetMap);
        mToolbar.setTitle("Wikibackpacker");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        singleCategoryDetails = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("singleCategoryDetails");
        category = getIntent().getStringExtra("category");

        customFont = Typeface.createFromAsset(getAssets(), "brown.ttf");

        setUpMapIfNeeded();
        intCatPOS = getCat(category);
        final TextView txtName = (TextView) findViewById(R.id.txtSelectedItemName);
        txtName.setTypeface(customFont);

        gallery = (Gallery) findViewById(R.id.galleryOSelectedCategory);
        picAdapter = new PicAdapter(this, singleCategoryDetails, 11);
        gallery.setAdapter(picAdapter);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtName.setText(singleCategoryDetails.get(position).get("name"));
                Double lat = Double.parseDouble(singleCategoryDetails.get(position).get("lat"));
                Double lon = Double.parseDouble(singleCategoryDetails.get(position).get("lon"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
                markers.get(position).showInfoWindow();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int[] screenLocation = new int[2];
                view.getLocationOnScreen(screenLocation);
                Intent subActivity = new Intent(SingleCategoryActivity.this,
                        DetailsActivity.class);
                int orientation = getResources().getConfiguration().orientation;
                subActivity
                        .putExtra("singleCategoryDetails", (Serializable) singleCategoryDetails)
                        .putExtra("selectedIndex", position)
                        .putExtra("category", category)
                        .putExtra("IMG_orientation", orientation).
                        putExtra("IMG_left", screenLocation[0]).
                        putExtra("IMG_top", screenLocation[1]).
                        putExtra("IMG_width", view.getWidth()).
                        putExtra("IMG_height", view.getHeight());
                startActivity(subActivity);
                overridePendingTransition(0, 0);

/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) singleCategoryDetails);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", category);
                startActivity(intent);*/
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                try {
                    LatLng center = mMap.getCameraPosition().target;

                    latTmp = String.valueOf(center.latitude);
                    lonTmp = String.valueOf(center.longitude);
//                    List<Address> addresses = geocoder.getFromLocation(center.latitude, center.longitude, 1);
//                    String address = addresses.get(0).getAddressLine(0);
//                    String city = addresses.get(0).getLocality();
//                    Log.e("MapDATA","latitude "+center.latitude+" longitude "+ center.longitude+" address "+address+" city "+city);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        try {
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap();
                }
            }
        } catch (Exception ex) {
            //this is to catch the maps throwing exception for lat lng builders
        }
    }

    /**
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        adjustPadding();

        markers = new ArrayList<Marker>();
        for (HashMap<String, String> hm : singleCategoryDetails) {
            double lat = Double.parseDouble(hm.get("lat"));
            double lon = Double.parseDouble(hm.get("lon"));
            String title = hm.get("name");
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title));
            markers.add(marker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        bounds = builder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }
        });
        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String markerTitle = marker.getTitle();
                int markerIndex = 0;
                for (int i = 0; i < singleCategoryDetails.size(); i++) {
                    if (singleCategoryDetails.get(i).get("name").equals(markerTitle)) {
                        markerIndex = i;
                        break;
                    }
                }
                gallery.setSelection(markerIndex, true);
                return true;
            }
        });
    }


    public void adjustPadding() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;
        mMap.setPadding(0, 0, 0, deviceHeight / 6 + 40);
    }

    public void parseJSONString(String jsonString) {
        singleCategoryDetails = new ArrayList<>();

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();
            switch (intCatPOS) {
                case 0:
                    jsonArray = jsonRootObject.optJSONArray("campgrounds");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                        hm.put("url", Constant.HOSTNAME + "viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=campgrounds");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 1:
                    jsonArray = jsonRootObject.optJSONArray("hostels");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=hostels");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 2:
                    jsonArray = jsonRootObject.optJSONArray("dayusearea");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=dayusearea");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 3:
                    jsonArray = jsonRootObject.optJSONArray("pointsofinterest");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=pois");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 4:
                    jsonArray = jsonRootObject.optJSONArray("infocenter");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=infocenter");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 5:
                    jsonArray = jsonRootObject.optJSONArray("toilets");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("tname"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=toilets");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 6:
                    jsonArray = jsonRootObject.optJSONArray("showers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("tname"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=showers");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 7:
                    jsonArray = jsonRootObject.optJSONArray("drinkingwater");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("tname"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=drinkingwater");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 8:
                    jsonArray = jsonRootObject.optJSONArray("caravanparks");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("displayName"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=caravanparks");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;
                case 9:
                    jsonArray = jsonRootObject.optJSONArray("bbqspots");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("id", jsonArray.getJSONObject(i).optString("id"));
                        hm.put("name", jsonArray.getJSONObject(i).optString("bbqName"));
                        hm.put("url", "http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=bbq");
                        hm.put("lat", jsonArray.getJSONObject(i).optString("lat"));
                        hm.put("lon", jsonArray.getJSONObject(i).optString("lon"));
                        hm.put("score", jsonArray.getJSONObject(i).optString("score"));
                        hm.put("rating", jsonArray.getJSONObject(i).optString("rating"));
                        hm.put("notes", jsonArray.getJSONObject(i).optString("notes"));
                        singleCategoryDetails.add(hm);
                    }
                    break;

            }


            picAdapter = new PicAdapter(this, singleCategoryDetails, 11);
            gallery.setAdapter(picAdapter);

            mMap.clear();
            setUpMap();
            progressBar.setVisibility(View.GONE);
            txtResetMap.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class JSONDownloader extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            JSONObject jsonObject = new JSONObject();
            OkHttpClient client = new OkHttpClient();
            try {
                String result = "";

//                Log.e("URL",""+urls[0]);
                Request request = new Request.Builder().url(urls[0]).build();
                Response response = client.newCall(request).execute();
                result = response.body().string();
                JSONArray jsonArray = new JSONArray(result);
                switch (intCatPOS) {
                    case 0:
                        jsonObject.put("campgrounds", jsonArray);
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
//                }
                return jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String jsonString) {
            parseJSONString(jsonString);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }


}
