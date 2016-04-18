package com.miracitechnology.wikibackpacker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SingleCategoryActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    List<HashMap<String,String>> singleCategoryDetails;
    int selectedIndex;
    LatLngBounds bounds;
    List<Marker> markers;
    Gallery gallery;

    Toolbar mToolbar;

    Typeface customFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category);

        mToolbar = (Toolbar)findViewById(R.id.mToolbar);
        mToolbar.setTitle("Wikibackpacker");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        singleCategoryDetails = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra("singleCategoryDetails");

        customFont = Typeface.createFromAsset(getAssets(),"brown.ttf");

        setUpMapIfNeeded();

        final TextView txtName = (TextView)findViewById(R.id.txtSelectedItemName);
        txtName.setTypeface(customFont);

        PicAdapter picAdapter = new PicAdapter(this,singleCategoryDetails,11);
        gallery = (Gallery)findViewById(R.id.galleryOSelectedCategory);
        gallery.setAdapter(picAdapter);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtName.setText(singleCategoryDetails.get(position).get("name"));
                Double lat = Double.parseDouble(singleCategoryDetails.get(position).get("lat"));
                Double lon = Double.parseDouble(singleCategoryDetails.get(position).get("lon"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lon)));
                markers.get(position).showInfoWindow();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)singleCategoryDetails);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        adjustPadding();

        markers = new ArrayList<Marker>();
        for (HashMap<String,String> hm : singleCategoryDetails)
        {
            double lat = Double.parseDouble(hm.get("lat"));
            double lon = Double.parseDouble(hm.get("lon"));
            String title = hm.get("name");
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title));
            markers.add(marker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers)
        {
            builder.include(marker.getPosition());
        }

        bounds = builder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,50));
            }
        });

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
                gallery.setSelection(markerIndex,true);
                return true;
            }
        });
    }

    public void adjustPadding()
    {
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;
        mMap.setPadding(0, 0, 0, deviceHeight / 6 + 40);
    }
}
