package com.ratemytoilet;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by spyder on 31/07/16.
 */

public class RateDetail extends AppCompatActivity implements OnMapReadyCallback {

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
    MapFragment mapFragment;
    TextView txtName;
    Gallery gallerySimilarPlaces;
    PicAdapter picAdapterSimilarPlaces;
    GoogleMap mGoogleMap;
    String title;
    double lat;
    double lon;


    public void runEnterAnimation() {
        final long duration = (long) (ANIM_DURATION * 1.5);
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


        ObjectAnimator colorizer = ObjectAnimator.ofFloat(RateDetail.this,
                "saturation", 0, 1);
        colorizer.setDuration(duration);
        colorizer.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_details);

        mToolbarDetails = (Toolbar) findViewById(R.id.mToolbarDetais);
        imgParallax = (ImageView) findViewById(R.id.imgParallaxTwo);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollViewBase = (LinearLayout) findViewById(R.id.scrollViewBase);
        title = getIntent().getStringExtra("title");
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapDetails);
        txtName = (TextView) findViewById(R.id.txtName);
        txtName.setTypeface(customFont);
        txtName.setText(title);
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int deviceWidth = size.x;
        int deviceHeight = size.y;
        imgParallax.setLayoutParams(new RelativeLayout.LayoutParams(deviceWidth, deviceHeight * 4 / 10));
        imgParallax.setScaleType(ImageView.ScaleType.FIT_XY);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, deviceHeight * 4 / 10, 0, 0);
        scrollViewBase.setLayoutParams(params);
        mapFragment.getMapAsync(this);
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
                    mLeftDelta = screenLocation[0];
                    mTopDelta = screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = 1;
                    mHeightScale = 1;

                    runEnterAnimation();

                    return true;
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setMyLocationEnabled(true);


        Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).title(title));
        marker.showInfoWindow();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));


    }
}
