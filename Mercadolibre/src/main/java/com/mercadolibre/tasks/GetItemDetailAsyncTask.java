package com.mercadolibre.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.mercadolibre.activities.AsyncTaskCompleteListener;
import com.mercadolibre.dto.Item;
import com.mercadolibre.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

public class GetItemDetailAsyncTask extends AsyncTask<String, Void, Item> {


    // Interface
    AsyncTaskCompleteListener callback;

    // JSON Node names
    static final String TAG_TITLE = "title";
    static final String TAG_PICTURES = "pictures";
    static final String TAG_URL_PIC = "url";
    static final String TAG_PRICE = "price";
    static final String TAG_AVAILABLE = "available_quantity";
    static final String TAG_ENDDATE = "stop_time";
    static String TAG_ID = "id";
    private static final String ML_BASE_API_URL = "https://api.mercadolibre.com";
    private static final String ML_SEARCH_RESOURCE = "/items/{0}";


    // pictures JSONArray
    static JSONArray pictures = null;

    // Hashmap for ListView
    ArrayList<Item> itemList;

    private String mUrl;

    public GetItemDetailAsyncTask(AsyncTaskCompleteListener listener, String query) {
        this.callback = listener;
        buildUrl(query);
    }

    private void buildUrl(String query){
        mUrl = ML_BASE_API_URL + MessageFormat.format(ML_SEARCH_RESOURCE, query);
    }

    @Override
    protected Item doInBackground(String... params) {

        String jsonStr =  Utils.getJSONString(mUrl);
        itemList = new ArrayList<Item>();
        Item item = null;


        if (jsonStr != null) {

            try {

                JSONObject jsonObj = new JSONObject(jsonStr);
                String title = jsonObj.getString(TAG_TITLE);
                Log.d("Title: ", "> " + title);

                // Getting JSON Array node
                pictures = jsonObj.getJSONArray(TAG_PICTURES);
                String endDate = jsonObj.getString(TAG_ENDDATE);

                JSONObject pic = pictures.getJSONObject(0);
                String urlPic = pic.getString(TAG_URL_PIC);
                Log.d("Picture: ", "> " + urlPic);

                String price = jsonObj.getString(TAG_PRICE);
                Log.d("Price: ", "> " + price);

                String currency = "$ ";
                price=currency+price;
                String available = "Cantidad disponible: " + jsonObj.getString(TAG_AVAILABLE);
                item = new Item(TAG_ID, title, price, urlPic, available, endDate);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return item;
    }

    @Override
    protected void onPostExecute(Item result) {
        super.onPostExecute(result);

         callback.onTaskComplete(result);
    }

}

