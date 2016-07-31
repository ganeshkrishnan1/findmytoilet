package com.ratemytoilet;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by soumyadip on 14-04-2016.
 */
public class CustomListViewAdapter extends BaseAdapter implements ListAdapter {

    private List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private Context context;

    int deviceWidth;
    int deviceHeight;

    Typeface customFont;

    public CustomListViewAdapter(List<HashMap<String,String>> list, Context context) {
        this.list = list;
        this.context = context;

        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        deviceHeight = size.y;

        customFont = Typeface.createFromAsset(context.getAssets(),"brown.ttf");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_layout, null);
        }

        ImageView imgPlace = (ImageView)view.findViewById(R.id.imgPlace);
        imgPlace.setLayoutParams(new LinearLayout.LayoutParams(deviceWidth, deviceHeight *4/10));
        imgPlace.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(list.get(position).get("url")).placeholder(android.R.drawable.progress_indeterminate_horizontal).into(imgPlace);

        TextView txtPrimary = (TextView)view.findViewById(R.id.txtPrimary);
        txtPrimary.setTypeface(customFont);
        txtPrimary.setText(Html.fromHtml("<br><b>" + list.get(position).get("name") + "</b>"));

        TextView txtSecondary = (TextView)view.findViewById(R.id.txtSecondary);
        txtSecondary.setTypeface(customFont);
        txtSecondary.setText(list.get(position).get("notes") + "\n\n");

        return view;
    }
}