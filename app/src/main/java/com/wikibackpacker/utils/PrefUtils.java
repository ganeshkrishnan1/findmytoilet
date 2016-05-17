package com.wikibackpacker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PrefUtils {

    //Pref Name
    public static final String PRF_Latitude = "current_Latitude";
    public static final String PRF_Longitude = "current_Longitude";

    // String Value Preference SET
    public static void setPref(Context c, String pref, String val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.apply();
    }

    // String Value Preference GET
    public static String getPref(Context c, String pref, String val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref,
                val);
    }

  /*  // boolean Value Preference SET
    public static void setPref(Context c, String pref, boolean val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.commit();

    }

    // boolean Value Preference GET
    public static boolean getPref(Context c, String pref, boolean val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(
                pref, val);
    }

    // Delete Preference
    public static void delPref(Context c, String pref) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.remove(pref);
        e.commit();
    }

    // int Value Preference SET
    public static void setPref(Context c, String pref, int val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val);
        e.commit();
    }

    // int Value Preference GET
    public static int getPref(Context c, String pref, int val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getInt(pref,
                val);
    }

    // long Value Preference SET
    public static void setPref(Context c, String pref, long val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val);
        e.commit();
    }

    // long Value Preference GET
    public static long getPref(Context c, String pref, long val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getLong(pref,
                val);
    }

    public static void setPref(Context c, String file, String pref, String val) {
        SharedPreferences settings = c.getSharedPreferences(file,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = settings.edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getPref(Context c, String file, String pref, String val) {
        return c.getSharedPreferences(file, Context.MODE_PRIVATE).getString(
                pref, val);
    }*/
}
