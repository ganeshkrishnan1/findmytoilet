package com.miracitechnology.wikibackpacker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        jsonString = getIntent().getStringExtra("jsonString");

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
        final TextView txtCampgroundsName = (TextView)findViewById(R.id.txtCampgroundsName);
        final TextView txtHostelsName = (TextView)findViewById(R.id.txtHostelsName);
        final TextView txtDayUseAreaName = (TextView)findViewById(R.id.txtDayUseAreaName);
        final TextView txtPointsOfInterestName = (TextView)findViewById(R.id.txtPointsOfInterestName);
        final TextView txtInfocenterName = (TextView)findViewById(R.id.txtInfocenterName);
        final TextView txtToiletsName = (TextView)findViewById(R.id.txtToiletsName);
        final TextView txtShowersName = (TextView)findViewById(R.id.txtShowersName);
        final TextView txtDrinkingWaterName = (TextView)findViewById(R.id.txtDrinkingWaterName);
        final TextView txtCaravanParksName = (TextView)findViewById(R.id.txtCaravanParksName);
        final TextView txtBBQSpotsName = (TextView)findViewById(R.id.txtBBQSpotsName);

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

        galleryOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listCampgrounds);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        galleryTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listHostels);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        galleryThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listDayUseArea);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        galleryFour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listPointsOfInterest);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        galleryFive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listInfocenter);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        gallerySix.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listToilets);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        gallerySeven.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listShowers);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        galleryEight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listDrinkingWater);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        galleryNine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listCaravanParks);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
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
        galleryTen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)listBBQSpots);
                intent.putExtra("selectedIndex",position);
                startActivity(intent);
            }
        });

        txtHomeMessage.setText("Hi, Rent unique places to stay from local hosts all over the world");
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
                listCaravanParks.add(hm);
            }

            jsonArray = jsonRootObject.optJSONArray("bbqspots");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                HashMap<String,String> hm = new HashMap<String,String>();
                hm.put("name",jsonArray.getJSONObject(i).optString("displayName"));
                hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id"));
                hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                listBBQSpots.add(hm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
