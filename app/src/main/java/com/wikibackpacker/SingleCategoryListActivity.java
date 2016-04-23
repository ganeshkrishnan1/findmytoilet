package com.wikibackpacker;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SingleCategoryListActivity extends AppCompatActivity {

    List<HashMap<String,String>> singleCategoryDetails;
    String category;
    boolean isLoading;

    CustomListViewAdapter customListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category_list);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color_500)));
        setTitle("Wikibackpacker");

        singleCategoryDetails = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra("singleCategoryDetails");
        category = getIntent().getStringExtra("category");

        isLoading = false;

        ListView listPlaces = (ListView)findViewById(R.id.listPlaces);
        customListViewAdapter = new CustomListViewAdapter(singleCategoryDetails,this);
        listPlaces.setAdapter(customListViewAdapter);

        listPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
                intent.putExtra("singleCategoryDetails",(Serializable)singleCategoryDetails);
                intent.putExtra("selectedIndex",position);
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });

        listPlaces.setOnScrollListener(new EndlessScrollListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_category_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_map) {
            Intent intent = new Intent(getApplicationContext(),SingleCategoryActivity.class);
            intent.putExtra("singleCategoryDetails",(Serializable)singleCategoryDetails);
            intent.putExtra("category",category);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 1;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                //new LoadGigsTask().execute(currentPage + 1);
                Toast.makeText(getApplicationContext(),"Loading more places..", Toast.LENGTH_SHORT).show();
                addMorePlaces(currentPage);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    public void addMorePlaces(int pageNo)
    {
        String apiToCall = "";
        if (category.equals("campgrounds"))
        {
            apiToCall = MainActivity.apiCampgrounds + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("hostels"))
        {
            apiToCall = MainActivity.apiHostels + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("dayusearea"))
        {
            apiToCall = MainActivity.apiDayUseArea + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("pois"))
        {
            apiToCall = MainActivity.apiPointsOfnterest + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("infocenter"))
        {
            apiToCall = MainActivity.apiInfoCenter + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("toilets"))
        {
            apiToCall = MainActivity.apiToilets + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("showers"))
        {
            apiToCall = MainActivity.apiShowers + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("drinkingwater"))
        {
            apiToCall = MainActivity.apiDrinkingWater + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("caravanparks"))
        {
            apiToCall = MainActivity.apiCaravanParks + "?page=" + String.valueOf(pageNo);
        }
        else if (category.equals("bbq"))
        {
            apiToCall = MainActivity.apiBBQSpots + "?page=" + String.valueOf(pageNo);
        }

        SingleCategoryJSONDownloader jsonDownloader = new SingleCategoryJSONDownloader();
        try {
            jsonDownloader.execute(apiToCall);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class SingleCategoryJSONDownloader extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            String result = "";
            try
            {
                Request request = new Request.Builder().url(urls[0]).build();
                Response response = client.newCall(request).execute();
                result = response.body().string();
                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    HashMap<String,String> hm = new HashMap<String,String>();
                    hm.put("id",jsonArray.getJSONObject(i).optString("id"));
                    hm.put("name",jsonArray.getJSONObject(i).optString("displayName"));
                    hm.put("url","http://api.wikibackpacker.com/api/viewAmenityImage/" + jsonArray.getJSONObject(i).optString("id") + "?default=campgrounds");
                    hm.put("lat",jsonArray.getJSONObject(i).optString("lat"));
                    hm.put("lon",jsonArray.getJSONObject(i).optString("lon"));
                    hm.put("score",jsonArray.getJSONObject(i).optString("score"));
                    hm.put("rating",jsonArray.getJSONObject(i).optString("rating"));
                    hm.put("notes",jsonArray.getJSONObject(i).optString("notes"));
                    singleCategoryDetails.add(hm);
                }
                customListViewAdapter.notifyDataSetChanged();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
