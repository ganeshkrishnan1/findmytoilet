package com.wikibackpacker;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

    //--
    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
    private static final int ANIM_DURATION = 500;
    List<HashMap<String, String>> singleCategoryDetails;
    String category;
    int selectedIndex;
    Toolbar mToolbarDetails;
    ImageView imgParallax;
    Typeface customFont;
    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;
    ScrollView scrollView;
    LinearLayout scrollViewBase;
    private BitmapDrawable mBitmapDrawable;
    private ColorMatrix colorizerMatrix = new ColorMatrix();
    //    private TextView mTextView;
    private int mOriginalOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //getActivity().getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color_500)));

        mToolbarDetails = (Toolbar) findViewById(R.id.mToolbarDetais);
        imgParallax = (ImageView) findViewById(R.id.imgParallaxTwo);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollViewBase = (LinearLayout) findViewById(R.id.scrollViewBase);

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
                if (id == R.id.action_share) {
                    String shareText = singleCategoryDetails.get(selectedIndex).get("name") + "\n";
                    shareText += "http://wikibackpacker.com/app/detail/" + category + "/" + singleCategoryDetails.get(selectedIndex).get("id");

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share Via.."));
                }
                return true;
            }
        });

        customFont = Typeface.createFromAsset(getAssets(), "brown.ttf");

        singleCategoryDetails = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("singleCategoryDetails");
        selectedIndex = getIntent().getIntExtra("selectedIndex", 0);
        category = getIntent().getStringExtra("category");
//s---

        mOriginalOrientation = getIntent().getIntExtra("IMG_orientation", 0);
        final int thumbnailLeft = getIntent().getIntExtra("IMG_left", 0);
        final int thumbnailTop = getIntent().getIntExtra("IMG_top", 0);
        final int thumbnailWidth = getIntent().getIntExtra("IMG_width", 0);
        final int thumbnailHeight = getIntent().getIntExtra("IMG_height", 0);
//        mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
//        mImageView.setImageDrawable(mBitmapDrawable);

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;
        imgParallax.setLayoutParams(new RelativeLayout.LayoutParams(deviceWidth, deviceHeight * 4 / 10));
        imgParallax.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(this).load(singleCategoryDetails.get(selectedIndex).get("url")).placeholder(android.R.drawable.progress_indeterminate_horizontal).into(imgParallax);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, deviceHeight * 4 / 10, 0, 0);
        scrollViewBase.setLayoutParams(params);

        if (savedInstanceState == null) {
            ViewTreeObserver observer = imgParallax.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    imgParallax.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    imgParallax.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) thumbnailWidth / imgParallax.getWidth();
                    mHeightScale = (float) thumbnailHeight / imgParallax.getHeight();

                    runEnterAnimation();

                    return true;
                }
            });
        }
//--
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapDetails);
        mapFragment.getMapAsync(this);


        TextView txtName = (TextView) findViewById(R.id.txtName);
        txtName.setTypeface(customFont);
        txtName.setText("\n" + singleCategoryDetails.get(selectedIndex).get("name") + "\n");

        TextView txtSimilarItem = (TextView) findViewById(R.id.txtSimilarItem);
        txtSimilarItem.setTypeface(customFont);
        txtSimilarItem.setTextColor(Color.WHITE);
        final TextView txtSimilarItemName = (TextView) findViewById(R.id.txtSimilarItemName);
        txtSimilarItemName.setTypeface(customFont);
        txtSimilarItemName.setTextColor(Color.WHITE);

        Gallery gallerySimilarPlaces = (Gallery) findViewById(R.id.gallerySimilarPlaces);
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
                intent.putExtra("singleCategoryDetails", (Serializable) singleCategoryDetails);
                intent.putExtra("selectedIndex", position);
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
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);

        double lat = Double.parseDouble(singleCategoryDetails.get(selectedIndex).get("lat"));
        double lon = Double.parseDouble(singleCategoryDetails.get(selectedIndex).get("lon"));
        String title = singleCategoryDetails.get(selectedIndex).get("name");
        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title));
        marker.showInfoWindow();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
    }

    public void runEnterAnimation() {
        final long duration = (long) (ANIM_DURATION * 1);
        imgParallax.setPivotX(0);
        imgParallax.setPivotY(0);
        imgParallax.setScaleX(mWidthScale);
        imgParallax.setScaleY(mHeightScale);
        imgParallax.setTranslationX(mLeftDelta);
        imgParallax.setTranslationY(mTopDelta);
        scrollView.setAlpha(0);
        imgParallax.animate().setDuration(duration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator).

                withEndAction(new Runnable() {
                    public void run() {
                        scrollView.setTranslationY(-scrollView.getHeight());
                        scrollView.animate().setDuration(duration / 2).
                                translationY(0).alpha(1).
                                setInterpolator(sDecelerator);
                    }
                });


        ObjectAnimator colorizer = ObjectAnimator.ofFloat(DetailsActivity.this,
                "saturation", 0, 1);
        colorizer.setDuration(duration);
        colorizer.start();

    }

    public void runExitAnimation(final Runnable endAction) {
        final long duration = (long) (ANIM_DURATION * 1);
        final boolean fadeOut;
        if (getResources().getConfiguration().orientation != mOriginalOrientation) {
            imgParallax.setPivotX(imgParallax.getWidth() / 2);
            imgParallax.setPivotY(imgParallax.getHeight() / 2);
            mLeftDelta = 0;
            mTopDelta = 0;
            fadeOut = true;
        } else {
            fadeOut = false;
        }

        scrollView.animate().translationY(-scrollView.getHeight()).alpha(0).
                setDuration(duration / 2).setInterpolator(sAccelerator).
                withEndAction(new Runnable() {
                    public void run() {
                        imgParallax.animate().setDuration(duration).
                                scaleX(mWidthScale).scaleY(mHeightScale).
                                translationX(mLeftDelta).translationY(mTopDelta).
                                withEndAction(endAction);
                        if (fadeOut) {
                            imgParallax.animate().alpha(0);
                        }

                        ObjectAnimator colorizer =
                                ObjectAnimator.ofFloat(DetailsActivity.this,
                                        "saturation", 1, 0);
                        colorizer.setDuration(duration);
                        colorizer.start();
                    }
                });


    }


    @Override
    public void onBackPressed() {
        runExitAnimation(new Runnable() {
            public void run() {
                finish();
            }
        });
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
