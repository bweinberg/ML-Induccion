package com.mercadolibre.activities;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.mercadolibre.dto.Item;
import com.mercadolibre.dto.Paging;
import com.mercadolibre.dto.Search;
import com.mercadolibre.fragments.ItemDetailFragment;
import com.mercadolibre.fragments.ListItemFragment;
import com.mercadolibre.fragments.SearchFragment;
import com.mercadolibre.services.NotificationService;
import com.mercadolibre.services.SearchService;
import com.mercadolibre.utils.Utils;
import java.util.ArrayList;
import java.util.Calendar;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity implements AsyncTaskCompleteListener, SearchFragment.SearchListener, ListItemFragment.ListItemListener, ItemDetailFragment.ItemDetailListener {

    // Search total. Convert to int.
    static String total = "";

    // Search in progress
    private static ProgressDialog pDialog;


    private static String mLastQuery;
    private MenuItem searchItem = null;

    // Hashmap for ListView
    private static ArrayList<Item> itemList = new ArrayList<Item>();

    private RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api.mercadolibre.com") // The base API endpoint.
            .build();
    private SearchService endpoint = restAdapter.create(SearchService.class);
    private static boolean inSettings = true;

    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        pDialog = new ProgressDialog(this);
        String newString;
        Bundle extras = getIntent().getExtras();

        if(extras == null) {
            newString= null;
        } else {
            newString= extras.getString("isNotification");
        }        //Add search fragment
        if(savedInstanceState==null){
            Fragment sf = SearchFragment.newInstance(Utils.getCurrentCountry(this));

            if(newString==null){
                //sets the "app is ready" notification every five minutes
                Calendar cal = Calendar.getInstance();
                AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, NotificationService.class);
                PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        300000, pintent);
                startService(intent);
                sf.setRetainInstance(true);
                addDynamicFragment(sf, R.id.layout_main_to_replace);
            }else{
                getFragmentManager().beginTransaction().add(R.id.layout_main_to_replace, sf, "SEARCH_FRAGMENT").addToBackStack("FRAGMENT_BACKSTACK").commit();
                onQuerySelected("ipod touch", Utils.getCurrentCountry(MainActivity.this));
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MenuItemCompat.collapseActionView(searchItem);
                onQuerySelected(query, Utils.getCurrentCountry(MainActivity.this));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        return true;
    }

    @Override
    public  boolean  onOptionsItemSelected(MenuItem item) {

        if(item.getTitle().equals("MercadoLibre")){
                backFromSettingsFragment();
        }
        return  super.onOptionsItemSelected(item);

    }



    public void addDynamicFragment(Fragment fg, int layout) {
        getFragmentManager().beginTransaction().add(layout, fg, "SEARCH_FRAGMENT").addToBackStack("FRAGMENT_BACKSTACK").commit();
        getActionBar().setDisplayHomeAsUpEnabled(false);

    }

    public void replaceDynamicFragment(Fragment fg, int layout, String tag) {
        getFragmentManager().beginTransaction().replace(layout, fg, tag).addToBackStack("FRAGMENT_BACKSTACK").commit();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onTaskComplete(ArrayList<Item> loadedList, String totalResults, String site, boolean loadMore) {


//        Update the list with data from the asynctask
        if (pDialog.isShowing())
            pDialog.dismiss();

        if (!loadMore) {
            itemList = loadedList;
        } else {
            for (int i = 0; i < loadedList.size(); i++) {
                itemList.add(loadedList.get(i));
            }
        }

        total = totalResults;
        SearchFragment searchFragment = (SearchFragment) getFragmentManager().findFragmentByTag("SEARCH_FRAGMENT");

        if (searchFragment== null || searchFragment.isVisible()) {


            Fragment lf = ListItemFragment.newInstance(itemList, total, mLastQuery, site);
            lf.setRetainInstance(true);
            replaceDynamicFragment(lf, R.id.layout_main_to_replace, "LIST_FRAGMENT");


        } else {

            ListItemFragment itemFragment = (ListItemFragment) getFragmentManager().findFragmentByTag("LIST_FRAGMENT");

            if (itemFragment.isVisible()) {
                itemFragment.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onTaskComplete(Item item) {

        // Update the list with data from the asynctask
        if (pDialog.isShowing())
            pDialog.dismiss();

        ListItemFragment listItemFragment = (ListItemFragment) getFragmentManager().findFragmentByTag("LIST_FRAGMENT");

        if (listItemFragment.isVisible()) {


            Fragment dif = ItemDetailFragment.newInstance(item);
            dif.setRetainInstance(true);
            replaceDynamicFragment(dif, R.id.layout_main_to_replace, "ITEM_FRAGMENT");
            searchItem.setVisible(false);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          //  getActionBar().setDisplayShowHomeEnabled(true);

        }

    }

    
    @Override
    public void onBackPressed()
    {
        searchItem.setVisible(true);
        backFromSettingsFragment();

    }

    private void backFromSettingsFragment()
    {
        getFragmentManager().popBackStackImmediate();
        SearchFragment homeFragment = (SearchFragment) getFragmentManager().findFragmentByTag("SEARCH_FRAGMENT");

        if(homeFragment.isVisible()){
            getActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public void onTaskComplete(Bitmap bitmap) {

        ItemDetailFragment itemDetailFragment = (ItemDetailFragment) getFragmentManager().findFragmentByTag("ITEM_FRAGMENT");
        if (itemDetailFragment.isVisible()) {
            itemDetailFragment.setImage(bitmap);
        }

    }


    @Override
    public void onQuerySelected(String query, final String site) {

        pDialog.setMessage("Cargando...");
        pDialog.show();

        mLastQuery = query;

        Callback callback = new Callback<Search>() {
            @Override
            public void success(Search result, Response response) {

                ArrayList<Item> items = result.getResults();
                Paging paging = result.getPaging();
                onTaskComplete(items, paging.getTotal(), result.getSite_id(), false);

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d("FAILURE!", retrofitError.toString());

            }
        };

        endpoint.getItems(site, query, callback);
        //new GetItemsAsyncTask(this, mLastQuery, site, false).execute();

    }

    @Override
    public void endOfListReached(int total, String site) {

        pDialog.setMessage("Cargando...");
        pDialog.show();


        Callback callback = new Callback<Search>() {
            @Override
            public void success(Search result, Response response) {
                ArrayList<Item> items = result.getResults();
                Paging paging = result.getPaging();
                onTaskComplete(items, paging.getTotal(), result.getSite_id(), true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d("FAILURE!", retrofitError.toString());
            }
        };


        endpoint.getMoreItems(site, mLastQuery, total, callback);
        //new GetItemsAsyncTask(this, mLastQuery, site, total, true).execute();

    }

    @Override
    public void onItemSelected(String id) {

        pDialog.setMessage("Cargando...");
        pDialog.show();

        Callback callback = new Callback<Item>() {

            @Override
            public void success(Item result, Response response) {
                onTaskComplete(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d("FAILURE!", retrofitError.toString());
            }

        };
        endpoint.getItem(id, callback);
        //new GetItemDetailAsyncTask(this, id).execute();

    }



}