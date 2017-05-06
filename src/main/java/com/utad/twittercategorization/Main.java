package com.utad.twittercategorization;

import com.utad.twittercategorization.utils.GeoUtils;

/**
 * Created by utad on 5/05/17.
 */
public class Main {

    public static void main(String [ ] args) {
        String response = GeoUtils.getCity("Madrid");
        System.out.println(response);
    }
}
