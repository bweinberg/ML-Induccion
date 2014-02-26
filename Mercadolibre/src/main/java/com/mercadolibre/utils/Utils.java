package com.mercadolibre.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.mercadolibre.activities.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by bweinberg on 21/01/14.
 */

public class Utils {
    public static String getJSONString(String url) {
        String jsonString = null;
        HttpURLConnection linkConnection = null;
        try {
            URL linkurl = new URL(url);
            linkConnection = (HttpURLConnection) linkurl.openConnection();
            int responseCode = linkConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream linkinStream = linkConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int j = 0;
                while ((j = linkinStream.read()) != -1) {
                    baos.write(j);
                }
                byte[] data = baos.toByteArray();
                jsonString = new String(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (linkConnection != null) {
                linkConnection.disconnect();
            }
        }
        return jsonString;
    }

    public static String getCurrentCountry(Activity context) {

        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        String site = "MLA";
        String countrySelected = sharedPref.getString(context.getString(R.string.preferred_country), "Argentina");
        if(countrySelected.equals("Brasil")){
            site = "MLB";
        }else if(countrySelected.equals("Argentina")){
            site = "MLA";
        }else if(countrySelected.equals("Uruguay")){
            site = "MLU";
        }

        return site;

    }

}

