package com.wikibackpacker.adapter;


import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.wikibackpacker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//-- AutoTextView Adapter
public class SuggestionAdapter extends ArrayAdapter<String> {
    protected static final String TAG = "SuggestionAdapter";
    final static String strURL = "http://api.wikibackpacker.com/api/getCities/";
    static String strWord = "";
    List<HashMap<String, String>> listCampgrounds;
    Activity mContext;
    private List<String> suggestions;

    public SuggestionAdapter(Activity context, String nameFilter) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        mContext = context;
        suggestions = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int index) {
        return suggestions.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.text_row, parent, false);
        }
        try {
            TextView txtCityName = (TextView) view.findViewById(R.id.txtCityName);
            String strFullName = listCampgrounds.get(position).get("city") + "," + listCampgrounds.get(position).get("country");
            String strCity = listCampgrounds.get(position).get("city");
            String strSelected = getString();
            SpannableStringBuilder builder = new SpannableStringBuilder();

            if (strSelected.length() > strCity.length()) {


                SpannableString redSpannable = new SpannableString(strFullName);
                redSpannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.cTextGreyDark)), 0, strSelected.length(), 0);
                redSpannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.cTextBlack)), strSelected.length(), strFullName.length(), 0);

                builder.append(redSpannable);
            } else {

                SpannableString redSpannable = new SpannableString(strFullName);
                redSpannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.cTextGreyDark)), 0, getString().length(), 0);
                redSpannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.cTextGrey)), getString().length(), strCity.length(), 0);
                redSpannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.cTextBlack)), strCity.length(), strFullName.length(), 0);

                builder.append(redSpannable);
            }


            txtCityName.setText(builder, TextView.BufferType.SPANNABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }

    public String getString() {
        return strWord;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    strWord = constraint.toString();

                    JSONObject jsonObject = new JSONObject();
                    OkHttpClient client = new OkHttpClient();

                    try {
                        Request request = new Request.Builder().url(strURL + constraint).build();
                        Response response = client.newCall(request).execute();
                        String result = response.body().string();
                        JSONArray jsonArray = new JSONArray(result);

                        jsonObject.put("cityData", jsonArray);

                        parseJSONString(jsonObject.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    suggestions.clear();
                    for (int i = 0; i < listCampgrounds.size(); i++) {
                        String strData = listCampgrounds.get(i).get("city") + "," + listCampgrounds.get(i).get("country");
                        suggestions.add(strData);
                    }

                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                } else {
                    strWord = "";
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }

    public void parseJSONString(String jsonString) {
        listCampgrounds = new ArrayList<HashMap<String, String>>();

        JSONObject jsonRootObject = null;
        try {
            jsonRootObject = new JSONObject(jsonString);

            JSONArray jsonArray = new JSONArray();


            jsonArray = jsonRootObject.optJSONArray("cityData");

            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> hm = new HashMap<String, String>();

                JSONArray jsonArrayNew = jsonArray.getJSONArray(i);

                hm.put("city", jsonArrayNew.optString(0));
                hm.put("country", jsonArrayNew.optString(1));

                listCampgrounds.add(hm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}