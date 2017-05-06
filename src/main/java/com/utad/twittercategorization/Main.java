package com.utad.twittercategorization;

import com.utad.twittercategorization.utils.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

/**
 * Created by utad on 5/05/17.
 */
public class Main {
    public static void main(String [ ] args) throws Exception {

        HashMap myEntities = EntitiesUtils.hagoGet("Bonito viaje en París, ha sido mejor que el de Londres. Me ha gustado el Louvre y los cuadros de Da Vinci");

        for (Object name: myEntities.keySet()){
            String key =name.toString();
            ArrayList value = (ArrayList) myEntities.get(name);
            System.out.println(key + " " + value);
        }
    }

}
