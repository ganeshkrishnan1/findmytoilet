package com.wikibackpacker;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wikibackpacker.adapter.NavDrawerListAdapter;
import com.wikibackpacker.utils.Constant;
import com.wikibackpacker.utils.NavDrawerItem;

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

    List<HashMap<String, String>> listCampgrounds;
    List<HashMap<String, String>> listHostels;
    List<HashMap<String, String>> listDayUseArea;
    List<HashMap<String, String>> listPointsOfInterest;
    List<HashMap<String, String>> listInfocenter;
    List<HashMap<String, String>> listToilets;
    List<HashMap<String, String>> listShowers;
    List<HashMap<String, String>> listDrinkingWater;
    List<HashMap<String, String>> listCaravanParks;
    List<HashMap<String, String>> listBBQSpots;
    Typeface customFont;
    private ListView mDrawerList;
    //    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    public void onClickCategory(View view) {
        Intent intent = new Intent(getApplicationContext(), SingleCategoryListActivity.class);

        int id = view.getId();
        switch (id) {
            case R.id.txtCampgrounds:
                intent.putExtra("singleCategoryDetails", (Serializable) listCampgrounds);
                intent.putExtra("category", "campgrounds");
                break;
            case R.id.txtHostels:
                intent.putExtra("singleCategoryDetails", (Serializable) listHostels);
                intent.putExtra("category", "hostels");
                break;
            case R.id.txtDayUseArea:
                intent.putExtra("singleCategoryDetails", (Serializable) listDayUseArea);
                intent.putExtra("category", "dayusearea");
                break;
            case R.id.txtPointsOfInterest:
                intent.putExtra("singleCategoryDetails", (Serializable) listPointsOfInterest);
                intent.putExtra("category", "pois");
                break;
            case R.id.txtInfocenter:
                intent.putExtra("singleCategoryDetails", (Serializable) listInfocenter);
                intent.putExtra("category", "infocenter");
                break;
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
            case R.id.txtCaravanParks:
                intent.putExtra("singleCategoryDetails", (Serializable) listCaravanParks);
                intent.putExtra("category", "caravanparks");
                break;
            case R.id.txtBBQSpots:
                intent.putExtra("singleCategoryDetails", (Serializable) listBBQSpots);
                intent.putExtra("category", "bbq");
                break;
        }

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color_500)));
//        setTitle("Wikibackpacker");

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
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));


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
                    case 0:
                        intent.putExtra("singleCategoryDetails", (Serializable) listCampgrounds);
                        intent.putExtra("category", "campgrounds");
                        break;
                    case 1:
                        intent.putExtra("singleCategoryDetails", (Serializable) listHostels);
                        intent.putExtra("category", "hostels");
                        break;
                    case 2:
                        intent.putExtra("singleCategoryDetails", (Serializable) listDayUseArea);
                        intent.putExtra("category", "dayusearea");
                        break;
                    case 3:
                        intent.putExtra("singleCategoryDetails", (Serializable) listPointsOfInterest);
                        intent.putExtra("category", "pois");
                        break;
                    case 4:
                        intent.putExtra("singleCategoryDetails", (Serializable) listInfocenter);
                        intent.putExtra("category", "infocenter");
                        break;
                    case 5:
                        intent.putExtra("singleCategoryDetails", (Serializable) listToilets);
                        intent.putExtra("category", "toilets");
                        break;
                    case 6:
                        intent.putExtra("singleCategoryDetails", (Serializable) listShowers);
                        intent.putExtra("category", "showers");
                        break;
                    case 7:
                        intent.putExtra("singleCategoryDetails", (Serializable) listDrinkingWater);
                        intent.putExtra("category", "drinkingwater");
                        break;
                    case 8:
                        intent.putExtra("singleCategoryDetails", (Serializable) listCaravanParks);
                        intent.putExtra("category", "caravanparks");
                        break;
                    case 9:
                        intent.putExtra("singleCategoryDetails", (Serializable) listBBQSpots);
                        intent.putExtra("category", "bbq");
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
        imgParallax.setLayoutParams(new LinearLayout.LayoutParams(deviceWidth, deviceHeight * 2 / 3));
        imgParallax.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(Constant.HOSTNAME + "viewAmenityImage/1587").into(imgParallax);

        listCampgrounds = new ArrayList<HashMap<String, String>>();
        listHostels = new ArrayList<HashMap<String, String>>();
        listDayUseArea = new ArrayList<HashMap<String, String>>();
        listPointsOfInterest = new ArrayList<HashMap<String, String>>();
        listInfocenter = new ArrayList<HashMap<String, String>>();
        listToilets = new ArrayList<HashMap<String, String>>();
        listShowers = new ArrayList<HashMap<String, String>>();
        listDrinkingWater = new ArrayList<HashMap<String, String>>();
        listCaravanParks = new ArrayList<HashMap<String, String>>();
        listBBQSpots = new ArrayList<HashMap<String, String>>();

        parseJSONString(jsonString);

        TextView txtHomeMessage = (TextView) findViewById(R.id.txtHomeMessage);
        txtHomeMessage.setTypeface(customFont);

        TextView txtCampgrounds = (TextView) findViewById(R.id.txtCampgrounds);
        txtCampgrounds.setTypeface(customFont);
        txtCampgrounds.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtHostels = (TextView) findViewById(R.id.txtHostels);
        txtHostels.setTypeface(customFont);
        txtHostels.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtDayUseArea = (TextView) findViewById(R.id.txtDayUseArea);
        txtDayUseArea.setTypeface(customFont);
        txtDayUseArea.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtPointsOfInterest = (TextView) findViewById(R.id.txtPointsOfInterest);
        txtPointsOfInterest.setTypeface(customFont);
        txtPointsOfInterest.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtInfocenter = (TextView) findViewById(R.id.txtInfocenter);
        txtInfocenter.setTypeface(customFont);
        txtInfocenter.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtToilets = (TextView) findViewById(R.id.txtToilets);
        txtToilets.setTypeface(customFont);
        txtToilets.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtShowers = (TextView) findViewById(R.id.txtShowers);
        txtShowers.setTypeface(customFont);
        txtShowers.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtDrinkingWater = (TextView) findViewById(R.id.txtDrinkingWater);
        txtDrinkingWater.setTypeface(customFont);
        txtDrinkingWater.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtCaravanParks = (TextView) findViewById(R.id.txtCaravanParks);
        txtCaravanParks.setTypeface(customFont);
        txtCaravanParks.setTextColor(getResources().getColor(R.color.title_text_color));
        TextView txtBBQSpots = (TextView) findViewById(R.id.txtBBQSpots);
        txtBBQSpots.setTypeface(customFont);
        txtBBQSpots.setTextColor(getResources().getColor(R.color.title_text_color));

        final TextView txtCampgroundsName = (TextView) findViewById(R.id.txtCampgroundsName);
        txtCampgroundsName.setTypeface(customFont);
        final TextView txtHostelsName = (TextView) findViewById(R.id.txtHostelsName);
        txtHostelsName.setTypeface(customFont);
        final TextView txtDayUseAreaName = (TextView) findViewById(R.id.txtDayUseAreaName);
        txtDayUseAreaName.setTypeface(customFont);
        final TextView txtPointsOfInterestName = (TextView) findViewById(R.id.txtPointsOfInterestName);
        txtPointsOfInterest.setTypeface(customFont);
        final TextView txtInfocenterName = (TextView) findViewById(R.id.txtInfocenterName);
        txtInfocenterName.setTypeface(customFont);
        final TextView txtToiletsName = (TextView) findViewById(R.id.txtToiletsName);
        txtToiletsName.setTypeface(customFont);
        final TextView txtShowersName = (TextView) findViewById(R.id.txtShowersName);
        txtShowersName.setTypeface(customFont);
        final TextView txtDrinkingWaterName = (TextView) findViewById(R.id.txtDrinkingWaterName);
        txtDrinkingWaterName.setTypeface(customFont);
        final TextView txtCaravanParksName = (TextView) findViewById(R.id.txtCaravanParksName);
        txtCaravanParksName.setTypeface(customFont);
        final TextView txtBBQSpotsName = (TextView) findViewById(R.id.txtBBQSpotsName);
        txtBBQSpotsName.setTypeface(customFont);

        Gallery galleryOne = (Gallery) findViewById(R.id.galleryOne);
        imgAdapterOne = new PicAdapter(this, listCampgrounds, 1);
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
        galleryOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listCampgrounds);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "campgrounds");
                startActivity(intent);*/

                pageTransform(view, listCampgrounds, position, "campgrounds");

            }
        });


        Gallery galleryTwo = (Gallery) findViewById(R.id.galleryTwo);
        imgAdapterTwo = new PicAdapter(this, listHostels, 2);
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
        galleryTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listHostels);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "hostels");
                startActivity(intent);*/
                pageTransform(view, listHostels, position, "hostels");

            }
        });

        Gallery galleryThree = (Gallery) findViewById(R.id.galleryThree);
        imgAdapterThree = new PicAdapter(this, listDayUseArea, 3);
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
        galleryThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listDayUseArea);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "dayusearea");
                startActivity(intent);*/
                pageTransform(view, listDayUseArea, position, "dayusearea");

            }
        });

        Gallery galleryFour = (Gallery) findViewById(R.id.galleryFour);
        imgAdapterFour = new PicAdapter(this, listPointsOfInterest, 4);
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
        galleryFour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listPointsOfInterest);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "pois");
                startActivity(intent);*/

                pageTransform(view, listPointsOfInterest, position, "pois");

            }
        });

        Gallery galleryFive = (Gallery) findViewById(R.id.galleryFive);
        imgAdapterFive = new PicAdapter(this, listInfocenter, 5);
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
        galleryFive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listInfocenter);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "infocenter");
                startActivity(intent);*/
                pageTransform(view, listInfocenter, position, "infocenter");

            }
        });

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
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listToilets);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "toilets");
                startActivity(intent);*/

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
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listShowers);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "showers");
                startActivity(intent);*/

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
//                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
//                intent.putExtra("singleCategoryDetails", (Serializable) listDrinkingWater);
//                intent.putExtra("selectedIndex", position);
//                intent.putExtra("category", "drinkingwater");
//                startActivity(intent);

                pageTransform(view, listDrinkingWater, position, "drinkingwater");

            }
        });

        Gallery galleryNine = (Gallery) findViewById(R.id.galleryNine);
        imgAdapterNine = new PicAdapter(this, listCaravanParks, 9);
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
        galleryNine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listCaravanParks);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "caravanparks");
                startActivity(intent);*/

                pageTransform(view, listCaravanParks, position, "caravanparks");

            }
        });

        Gallery galleryTen = (Gallery) findViewById(R.id.galleryTen);
        imgAdapterTen = new PicAdapter(this, listBBQSpots, 10);
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
        galleryTen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("singleCategoryDetails", (Serializable) listBBQSpots);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("category", "bbq");
                startActivity(intent);*/

                pageTransform(view, listBBQSpots, position, "bbq");

            }
        });

        txtHomeMessage.setText("\nLive Love Travel");
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

/*        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void parseJSONString(String jsonString) {
        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray();

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
                listCampgrounds.add(hm);
            }

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
                listHostels.add(hm);
            }

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
                listDayUseArea.add(hm);
            }

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
                listPointsOfInterest.add(hm);
            }

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
                listInfocenter.add(hm);
            }

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
                listToilets.add(hm);
            }

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
                listShowers.add(hm);
            }

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
                listDrinkingWater.add(hm);
            }

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
                listCaravanParks.add(hm);
            }

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
                listBBQSpots.add(hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private void setupDrawer()
    {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer,R.string.drawer_open, R.string.drawer_close) {

            *//** Called when a drawer has settled in a completely open state. *//*
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            */

    /**
     * Called when a drawer has settled in a completely closed state.
     *//*
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
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
    }*/

}
