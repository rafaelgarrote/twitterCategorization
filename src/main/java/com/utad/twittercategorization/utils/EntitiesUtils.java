package com.utad.twittercategorization.utils;

import org.json.JSONObject;
import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by utad on 5/05/17.
 */
public class EntitiesUtils {

    public static HashMap hagoGet(String texto) throws Exception {

        HashMap myEntities = new HashMap();
        ArrayList myTypes;

        //Variable que sirve para hacer pruebas del método
        String MiTweet = URLEncoder.encode(texto);
        String resultado = HTTPUtils.getHTML("https://api.dandelion.eu/datatxt/nex/v1/?min_confidence=0.6&social=False&text="+MiTweet+"&include=image%2Cabstract%2Ctypes%2Ccategories%2Clod&country=-1&token=686740f7c1f645d8b746139d822e078f");

        System.out.println(resultado);

        JSONObject obj = new JSONObject(resultado);

        //Imprime las etiquetas de primer nivel del JSON
        /*
        JSONArray etiquetas = obj.names();
        for (int i = 0; i < etiquetas.length(); i++) {
            System.out.println(etiquetas.getString(i));
        }
        */

        //Creamos un array con el contenido de la etiqueta "Annotations", que a su vez contiene un JSON
        JSONArray arr = obj.getJSONArray("annotations");

        //Analizamos cada elemento del array, buscamos las etiquetas e imprimimos sus valores.
        for (int i = 0; i < arr.length(); i++) {

            myTypes = new ArrayList();
            JSONObject obj2 = arr.getJSONObject(i);

            String s = obj2.getString("spot");
            Double c = obj2.getDouble("confidence");
            String t = obj2.getString("title");

            //System.out.println("La palabra "+s+" es la entidad "+t+" con una confianza de "+c);

            //Los tipos de una entidad son mostrados como URL y contenidas en un Array
            JSONArray types = obj2.getJSONArray("types");

            //Para cada entidad recorremos su array de URL, y cada URL la spliteamos y nos quedamos con la última palabra que nos dice el tipo.
            for (int j = 0; j < types.length(); j++) {
                String[] url = types.getString(j).split("/");
                String lastUrl = url[url.length-1];
                myTypes.add(lastUrl);
            }

            //Añadimos a un HashMap el nombre de la entidad como clave y como valor, un array de tipos detectados.
            myEntities.put(t, myTypes);
        }

        /*
        //Imprimir elementos del HashMap donde tenemos las entidades y sus tipos
        for (Object name: myEntities.keySet()){
            String key =name.toString();
            ArrayList value = (ArrayList) myEntities.get(name);
            System.out.println(key + " " + value);
        }
        */

        return myEntities;
    }
}
