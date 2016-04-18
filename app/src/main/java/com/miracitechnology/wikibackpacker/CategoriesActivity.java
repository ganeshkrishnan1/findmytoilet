package com.miracitechnology.wikibackpacker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoriesActivity extends AppCompatActivity {

    PicAdapter imgAdapterOne;
    PicAdapter imgAdapterTwo;
    PicAdapter imgAdapterThree;
    PicAdapter imgAdapterFour;
    PicAdapter imgAdapterFive;
    PicAdapter imgAdapterSix;
    PicAdapter imgAdapterSeven;
    PicAdapter imgAdapterEight;
    PicAdapter imgAdapterNine;
    PicAdapter imgAdapterTen;

    String jsonString;

    List<HashMap<String,String>> listCampgrounds;
    List<HashMap<String,String>> listHostels;
    List<HashMap<String,String>> listDayUseArea;
    List<HashMap<String,String>> listPointsOfInterest;
    List<HashMap<String,String>> listInfocenter;
    List<HashMap<String,String>> listToilets;
    List<HashMap<String,String>> listShowers;
    List<HashMap<String,String>> listDrinkingWater;
    List<HashMap<String,String>> listCaravanParks;
    List<HashMap<String,String>> listBBQSpots;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private  String mActivityTitle;

    Typeface customFont;

    public void onClickCategory(View view)
    {
        Intent intent = new Intent(getApplicationContext(),SingleCategoryListActivity.class);

        int id = view.getId();
        switch (id)
        {
            case R.id.txtCampgrounds:
                intent.putExtra("singleCategoryDetails",(Serializable)listCampgrounds);
                break;
            case R.id.txtHostels:
                intent.putExtra("singleCategoryDetails",(Serializable)listHostels);
                break;
            case R.id.txtDayUseArea:
                intent.putExtra("singleCategoryDetails",(Serializable)listDayUseArea);
                break;
            case R.id.txtPointsOfInterest:
                intent.putExtra("singleCategoryDetails",(Serializable)listPointsOfInterest);
                break;
            case R.id.txtInfocenter:
                intent.putExtra("singleCategoryDetails",(Serializable)listInfocenter);
                break;
            case R.id.txtToilets:
                intent.putExtra("singleCategoryDetails",(Serializable)listToilets);
                break;
            case R.id.txtShowers:
                intent.putExtra("singleCategoryDetails",(Serializable)listShowers);
                break;
            case R.id.txtDrinkingWater:
                intent.putExtra("singleCategoryDetails",(Serializable)listDrinkingWater);
                break;
            case R.id.txtCaravanParks:
                intent.putExtra("singleCategoryDetails",(Serializable)listCaravanParks);
                break;
            case R.id.txtBBQSpots:
                intent.putExtra("singleCategoryDetails",(Serializable)listBBQSpots);
                break;
        }

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color_500)));

        customFont = Typeface.createFromAsset(getAssets(),"brown.ttf");

        jsonString = getIntent().getStringExtra("jsonString");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mActivityTitle = getSupportActionBar().getTitle().toString();
        List<String> listDrawer = new ArrayList<String>();
        listDrawer.add("Option 1");
        listDrawer.add("Option 2");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listDrawer);
        mDrawerList.setAdapter(arrayAdapter);
        setupDrawer();

        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;
        ImageView imgParallax = (ImageView)findViewById(R.id.imgParallax);
        imgParallax.setLayoutParams(new LinearLayout.LayoutParams(deviceWidth,deviceHeight*2/3));
        imgParallax.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load("http://api.wikibackpacker.com/api/viewAmenityImage/1587").into(imgParallax);

        listCampgrounds = new ArrayList<HashMap<String,String>>();
        listHostels = new ArrayList<HashMap<String,String>>();
        listDayUseArea = new ArrayList<HashMap<String,String>>();
        listPointsOfInterest = new ArrayList<HashMap<String,String>>();
        listInfocenter = new ArrayList<HashMap<String,String>>();
        listToilets = new ArrayList<HashMap<String,String>>();
        listShowers = new ArrayList<HashMap<String,String>>();
        listDrinkingWater = new ArrayList<HashMap<String,String>>();
        listCaravanParks = new ArrayList<HashMap<String,String>>();
        listBBQSpots = new ArrayList<HashMap<String,String>>();

        parseJSONString(jsonString);

        TextView txtHomeMessage = (TextView)findViewById(R.id.txtHomeMessage);
        txtHomeMessage.setTypeface(customFont);

        TextView txtCampgrounds = (TextView)findViewById(R.id.txtCampgrounds);
        txtCampgrounds.setTypeface(customFont);
        TextView txtHostels = (TextView)findViewById(R.id.txtHostels);
        txtHostels.setTypeface(customFont);
        TextView txtDayUseArea = (TextView)findViewById(R.id.txtDayUseArea);
        txtDayUseArea.setTypeface(customFont);
        TextView txtPointsOfInterest = (TextView)findViewById(R.id.txtPointsOfInterest);
        txtPointsOfInterest.setTypeface(customFont);
        TextView txtInfocenter = (TextView)findViewById(R.id.txtInfocenter);
        txtInfocenter.setTypeface(customFont);
        TextView txtToilets = (TextView)findViewById(R.id.txtToilets);
        txtToilets.setTypeface(customFont);
        TextView txtShowers = (TextView)findViewById(R.id.txtShowers);
        txtShowers.setTypeface(customFont);
        TextView txtDrinkingWater = (TextView)findViewById(R.id.txtDrinkingWater);
        txtDrinkingWater.setTypeface(customFont);
        TextView txtCaravanParks = (TextView)findViewById(R.id.txtCaravanParks);
        txtCaravanParks.setTypeface(customFont);
        TextView txtBBQSpots = (TextView)findViewById(R.id.txtBBQSpots);
        txtBBQSpots.setTypeface(customFont);

        final TextView txtCampgroundsName = (TextView)findViewById(R.id.txtCampgroundsName);
        txtCampgroundsName.setTypeface(customFont);
        final TextView txtHostelsName = (TextView)findViewById(R.id.txtHostelsName);
        txtHostelsName.setTypeface(customFont);
        final TextView txtDayUseAreaName = (TextView)findViewById(R.id.txtDayUseAreaName);
        txtDayUseAreaName.setTypeface(customFont);
        final TextView txtPointsOfInterestName = (TextView)findViewById(R.id.txtPointsOfInterestName);
        txtPointsOfInterest.setTypeface(customFont);
        final TextView txtInfocenterName = (TextView)findViewById(R.id.txtInfocenterName);
        txtInfocenterName.setTypeface(customFont);
        final TextView txtToiletsName = (TextView)findViewById(R.id.txtToiletsName);
        txtToiletsName.setTypeface(customFont);
        final TextView txtShowersName = (TextView)findViewById(R.id.txtShowersName);
        txtShowersName.setTypeface(customFont);
        final TextView txtDrinkingWaterName = (TextView)findViewById(R.id.txtDrinkingWaterName);
        txtDrinkingWaterName.setTypeface(customFont);
        final TextView txtCaravanParksName = (TextView)findViewById(R.id.txtCaravanParksName);
        txtCaravanParksName.setTypeface(customFont);
        final TextView txtBBQSpotsName = (TextView)findViewById(R.id.txtBBQSpotsName);
        txtBBQSpotsName.setTypeface(customFont);

        Gallery galleryOne = (Gallery)findViewById(R.id.galleryOne);
        imgAdapterOne = new PicAdapter(this,listCampgrounds,1);
        galleryOne.setAdapter(imgAdapterOne);
        galleryOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCampgroundsName.setText("\n" + imgAdapterOne.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Gallery galleryTwo = (Gallery)findViewById(R.id.galleryTwo);
        imgAdapterTwo = new PicAdapter(this,listHostels,2);
        galleryTwo.setAdapter(imgAdapterTwo);
        galleryTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtHostelsName.setText("\n" + imgAdapterTwo.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Gallery galleryThree = (Gallery)findViewById(R.id.galleryThree);
        imgAdapterThree = new PicAdapter(this,listDayUseArea,3);
        galleryThree.setAdapter(imgAdapterThree);
        galleryThree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtDayUseAreaName.setText("\n" + imgAdapterThree.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Gallery galleryFour = (Gallery)findViewById(R.id.galleryFour);
        imgAdapterFour = new PicAdapter(this,listPointsOfInterest,4);
        galleryFour.setAdapter(imgAdapterFour);
        galleryFour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtPointsOfInterestName.setText("\n" + imgAdapterFour.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Gallery galleryFive = (Gallery)findViewById(R.id.galleryFive);
        imgAdapterFive = new PicAdapter(this,listInfocenter,5);
        galleryFive.setAdapter(imgAdapterFive);
        galleryFive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtInfocenterName.setText("\n" + imgAdapterFive.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Gallery gallerySix = (Gallery)findViewById(R.id.gallerySix);
        imgAdapterSix = new PicAdapter(this,listToilets,6);
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

        Gallery gallerySeven = (Gallery)findViewById(R.id.gallerySeven);
        imgAdapterSeven = new PicAdapter(this,listShowers,7);
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

        Gallery galleryEight = (Gallery)findViewById(R.id.galleryEight);
        imgAdapterEight = new PicAdapter(this,listDrinkingWater,8);
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

        Gallery galleryNine = (Gallery)findViewById(R.id.galleryNine);
        imgAdapterNine = new PicAdapter(this,listCaravanParks,9);
        galleryNine.setAdapter(imgAdapterNine);
        galleryNine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtCaravanParksName.setText("\n" + imgAdapterNine.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Gallery galleryTen = (Gallery)findViewById(R.id.galleryTen);
        imgAdapterTen = new PicAdapter(this,listBBQSpots,10);
        galleryTen.setAdapter(imgAdapterTen);
        galleryTen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtBBQSpotsName.setText("\n" + imgAdapterTen.getImageName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtHomeMessage.setText("\nHi, Rent unique places to stay from local hosts all over the world");
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
        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void parseJSONString(String jsonString)
    {
        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();

            jsonArray = jsonRootObject.optJSONArray("campgrounds");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listCampgrounds.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("hostels");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listHostels.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("dayusearea");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listDayUseArea.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("pointsofinterest");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listPointsOfInterest.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("infocenter");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listInfocenter.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("toilets");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("tname"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listToilets.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("showers");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("tname"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listShowers.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("drinkingwater");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("tname"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listDrinkingWater.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("caravanparks");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listCaravanParks.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("bbqspots");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("bbqName"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                listBBQSpots.add(hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDrawer()
    {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer,R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
