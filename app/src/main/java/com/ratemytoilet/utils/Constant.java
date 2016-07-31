package com.ratemytoilet.utils;

/**
 * Created by spyder on 20/04/16.
 */
public class Constant {

    public static final String HOSTNAME="http://www.backpackwiki.com/api/";

    public static final String getAPIByLatLon(float lat, float lon)
    {

        return HOSTNAME + "/" + lat + "/" + lon;
    }

    //Eg "Melbourne,AU"
    public static final String getAPIByCityAndCountry(String city, String country)
    {
        return HOSTNAME+"?location="+city + "," + country;
    }
}
