package com.wikibackpacker;

/**
 * Created by soumyadip on 09-04-2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

public class PicAdapter extends BaseAdapter {

    ProgressDialog progress;

    //Device Height and Width
    int deviceWidth;
    int deviceHeight;

    //gallery context
    private Context galleryContext;

    //placeholder bitmap for empty spaces in gallery
    Bitmap placeholder;

    List<HashMap<String,String>> images;

    int currentImgAdapter;

    public PicAdapter(Context context, List<HashMap<String,String>> images, int imgAdapterNo)
    {

        progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        //instantiate context
        galleryContext = context;

        this.images = images;

        this.currentImgAdapter = imgAdapterNo;

        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        deviceHeight = size.y;

        //decode the placeholder image
        //placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.abc_ab_share_pack_holo_dark );
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //create the view
        ImageView imageView = new ImageView(galleryContext);
        //specify the bitmap at this position in the array
        //imageView.setImageBitmap(imageBitmap);
        Glide.with(galleryContext).load(images.get(position).get("url")).placeholder(R.drawable.spinner).into(imageView);
        //set layout options
        if (currentImgAdapter >= 11)
        {
            imageView.setLayoutParams(new Gallery.LayoutParams(deviceWidth /3, deviceHeight /6));
        }
        else
        {
            imageView.setLayoutParams(new Gallery.LayoutParams(deviceWidth * 9 / 10, deviceHeight * 4 / 10));
        }
        //scale type within view area
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //set default gallery item background
        //imageView.setBackgroundResource(defaultItemBackground);
        //return the view
        return imageView;
    }

    public String getImageName(int position)
    {
        return images.get(position).get("name");
    }
}
