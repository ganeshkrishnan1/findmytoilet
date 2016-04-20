package com.wikibackpacker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    List<HashMap<String,String>> singleCategoryDetails;
    String category;
    int selectedIndex;
    Toolbar mToolbarDetails;
    ImageView imgParallax;
    Typeface customFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //getActivity().getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color_500)));

        mToolbarDetails = (Toolbar)findViewById(R.id.mToolbarDetais);
        //mToolbarDetails.setTitle("Wikibackpacker");
        //mToolbarDetails.setTitleTextColor(Color.WHITE);
        mToolbarDetails.setBackgroundColor(Color.TRANSPARENT);
        mToolbarDetails.setNavigationIcon(R.drawable.ic_action_back);
        mToolbarDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbarDetails.inflateMenu(R.menu.menu_details);
        mToolbarDetails.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.action_share)
                {
                    String shareText = singleCategoryDetails.get(selectedIndex).get("name") + "\n";
                    shareText += "http://wikibackpacker.com/app/detail/" + category + "/" + singleCategoryDetails.get(selectedIndex).get("id");

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share Via.."));
                }
                return  true;
            }
        });

        customFont = Typeface.createFromAsset(getAssets(),"brown.ttf");

        singleCategoryDetails = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra("singleCategoryDetails");
        selectedIndex = getIntent().getIntExtra("selectedIndex",0);
        category = getIntent().getStringExtra("category");

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapDetails);
        mapFragment.getMapAsync(this);

        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;
        imgParallax = (ImageView)findViewById(R.id.imgParallaxTwo);
        imgParallax.setLayoutParams(new RelativeLayout.LayoutParams(deviceWidth, deviceHeight * 4 / 10));
        imgParallax.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(singleCategoryDetails.get(selectedIndex).get("url")).into(imgParallax);

        TextView txtName = (TextView)findViewById(R.id.txtName);
        txtName.setTypeface(customFont);
        txtName.setText("\n" + singleCategoryDetails.get(selectedIndex).get("name") + "\n");

        TextView txtSimilarItem = (TextView)findViewById(R.id.txtSimilarItem);
        txtSimilarItem.setTypeface(customFont);
        txtSimilarItem.setTextColor(Color.WHITE);
        final TextView txtSimilarItemName = (TextView)findViewById(R.id.txtSimilarItemName);
        txtSimilarItemName.setTypeface(customFont);
        txtSimilarItemName.setTextColor(Color.WHITE);

        Gallery gallerySimilarPlaces = (Gallery)findViewById(R.id.gallerySimilarPlaces);
        PicAdapter picAdapterSimilarPlaces = new PicAdapter(this, singleCategoryDetails, 12);
        gallerySimilarPlaces.setAdapter(picAdapterSimilarPlaces);
        gallerySimilarPlaces.setFocusable(false);
        gallerySimilarPlaces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtSimilarItemName.setText(singleCategoryDetails.get(position).get("name"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gallerySimilarPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.putExtra("singleCategoryDetails",(Serializable)singleCategoryDetails);
                intent.putExtra("selectedIndex",position);
                finish();
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat = Double.parseDouble(singleCategoryDetails.get(selectedIndex).get("lat"));
        double lon = Double.parseDouble(singleCategoryDetails.get(selectedIndex).get("lon"));
        String title = singleCategoryDetails.get(selectedIndex).get("name");
        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title));
        marker.showInfoWindow();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),15));
    }
}
