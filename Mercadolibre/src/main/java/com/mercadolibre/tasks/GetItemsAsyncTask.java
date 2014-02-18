package com.mercadolibre.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.mercadolibre.activities.AsyncTaskCompleteListener;
import com.mercadolibre.dto.Item;
import com.mercadolibre.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;

public class GetItemsAsyncTask extends AsyncTask<Void, Void, ArrayList> {
    private static String ML_BASE_API_URL = "https://api.mercadolibre.com";
    private static String ML_SEARCH_RESOURCE = "/sites/{0}/search?q={1}";

    // Interface
    AsyncTaskCompleteListener callback;

    // JSON Node names
    static final String TAG_TITLE = "title";
    static final String TAG_TOTAL = "total";
    static final String TAG_PAGING = "paging";
    static final String TAG_PRICE = "price";
    static final String TAG_RESULTS = "results";
    static final String TAG_THUMBNAIL = "thumbnail";
    static final String TAG_URL_PIC = "url";


    static String siteId = "";

    static String TAG_ID = "id";
    static String total = "";

    // items JSONArray
    static JSONArray items = null;
    static JSONObject paging = null;
    static boolean loadMore;

    // Hashmap for ListView
    ArrayList<Item> itemList;
    // pictures JSONArray
    static JSONArray pictures = null;

    static String mUrl;

    public GetItemsAsyncTask(AsyncTaskCompleteListener listener, String query, String site, boolean endOfList) {

        this.callback = listener;
        loadMore = endOfList;
        siteId = site;

        try {
            query = URLEncoder.encode(query, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        buildUrl(query, site);
    }


    public GetItemsAsyncTask(AsyncTaskCompleteListener listener, String query, String site, int count, boolean endOfList) {

        try {
            query = URLEncoder.encode(query, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        query += "&offset=" + count;
        Log.d("GetItemsAsyncTask", "query" + query);
        loadMore = endOfList;
        this.callback = listener;
        buildUrl(query, site);
    }

    private void buildUrl(String query, String site){


        mUrl = ML_BASE_API_URL + MessageFormat.format(ML_SEARCH_RESOURCE, site, query);
        Log.d("setOnScrollListener", "mUrl " + mUrl);

    }

    @Override
    protected ArrayList doInBackground(Void... params) {

        Log.d("GetItemsAsyncTask", "doInBackground");

        String jsonStr =  Utils.getJSONString(mUrl);
        itemList = new ArrayList<Item>();


        if (jsonStr != null) {

            try {

                JSONObject jsonObj = new JSONObject(jsonStr);
                paging = jsonObj.getJSONObject(TAG_PAGING);
                total = paging.getString(TAG_TOTAL);
                items = jsonObj.getJSONArray(TAG_RESULTS);


                for(int i=0; i<items.length(); i++){

                    JSONObject it = items.getJSONObject(i);
                    String title = it.getString(TAG_TITLE);
                    String id = it.getString(TAG_ID);
                    String price = it.getString(TAG_PRICE);
                    String urlPic = it.getString(TAG_THUMBNAIL);
                    Log.d("Picture: ", "> " + urlPic);

                    String currency = "$ ";
                    price=currency+price;
                    Item item = new Item(id, title, price, urlPic, null, null);

                    itemList.add(item);

                }

                return itemList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList result) {

        Log.e("onPostExecute", "onPostExecute");
        callback.onTaskComplete(result, total, siteId, loadMore);

    }

}

