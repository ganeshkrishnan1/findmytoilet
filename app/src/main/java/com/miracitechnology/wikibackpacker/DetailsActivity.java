package com.miracitechnology.wikibackpacker;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DetailsActivity extends ActionBarActivity {

    List<HashMap<String,String>> singleCategoryDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color_500)));
        singleCategoryDetails = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra("singleCategoryDetails");
        int selectedIndex = getIntent().getIntExtra("selectedIndex",0);

        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;
        ImageView imgParallax = (ImageView)findViewById(R.id.imgParallaxTwo);
        imgParallax.setLayoutParams(new LinearLayout.LayoutParams(deviceWidth,deviceHeight/3));
        imgParallax.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(singleCategoryDetails.get(selectedIndex).get("url")).into(imgParallax);
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
}
