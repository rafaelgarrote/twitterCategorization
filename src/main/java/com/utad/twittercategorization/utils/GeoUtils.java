package com.utad.twittercategorization.utils;

import org.json.JSONObject;

/**
 * Created by utad on 5/05/17.
 */
public class GeoUtils {

    private static String GOOGLE_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private static String SENSOR = "&sensor=false";

    public static String getCity(String latitude, String longitude) {

        String result = "";
        try {
            String url = GOOGLE_URL + latitude + "," + longitude + SENSOR;
            result = HTTPUtils.getHTML(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        parseResult(result);
        return result;

    }

    private static String parseResult(String result) {
        JSONObject obj = new JSONObject(result);
        JSONObject ll = (JSONObject) obj.getJSONArray("results").get(0);
        ll.getJSONArray("address_components");
        return "";
    }

}
