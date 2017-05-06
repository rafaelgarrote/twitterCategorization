package com.utad.twittercategorization.utils;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by utad on 5/05/17.
 */
public class GeoUtils {

    private static String GOOGLE_URL = "http://maps.googleapis.com/maps/api/geocode/json?";
    private static String QUERY_STRING = "address=";
    private static String SENSOR = "&sensor=false";

    public static String getCity(ArrayList<String> query) {
        String queryString = "";
        for(String q: query) {
            queryString = queryString + q + "+";
        }
        return queryString.substring(0, queryString.length() - 1);
    }

    public static String getCity(String query) {

        String result = "";
        try {
            String queryString = QUERY_STRING + URLEncoder.encode(query) + SENSOR;
            String url = GOOGLE_URL + queryString;
            System.out.println(url);
            String response = HTTPUtils.getHTML(url);
            result = parseResult(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    private static String parseResult(String result) {
        JSONObject obj = new JSONObject(result);
        JSONObject results = (JSONObject) obj.getJSONArray("results").get(0);
        JSONObject location = results.getJSONObject("geometry").getJSONObject("location");
        Double latitude = location.getDouble("lat");
        Double longitude = location.getDouble("lng");
        return latitude + "," + longitude;
    }

}
