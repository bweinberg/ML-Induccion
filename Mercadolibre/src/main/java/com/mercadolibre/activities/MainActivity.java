package com.mercadolibre.activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.mercadolibre.dto.Item;
import com.mercadolibre.dto.Paging;
import com.mercadolibre.dto.Search;
import com.mercadolibre.fragments.CountrySelectorFragment;
import com.mercadolibre.fragments.ItemDetailFragment;
import com.mercadolibre.fragments.ListItemFragment;
import com.mercadolibre.fragments.SearchFragment;
import com.mercadolibre.services.SearchService;
import com.mercadolibre.tasks.GetImagesAsyncTask;
import com.mercadolibre.utils.Utils;
import java.util.ArrayList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity implements AsyncTaskCompleteListener, SearchFragment.SearchListener, ListItemFragment.ListItemListener, ItemDetailFragment.ItemDetailListener, CountrySelectorFragment.CountrySelectorListener {

    private String[] mmenuList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    // Search total. Convert to int.
    static String total = "";

    // Search in progress
    private static ProgressDialog pDialog;


    private static String mLastQuery;
    private static String TAG_REPLACE = "";
    private MenuItem searchItem = null;
    private ActionBarDrawerToggle mDrawerToggle;

    // Hashmap for ListView
    private static ArrayList<Item> itemList = new ArrayList<Item>();
    RestAdapter restAdapter = new RestAdapter.Builder()
            .setServer("https://api.mercadolibre.com") // The base API endpoint.
            .build();
    SearchService endpoint = restAdapter.create(SearchService.class);
    private static String site;
    private static boolean inSettings = true;

    public void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        pDialog = new ProgressDialog(this);

//        mmenuList = new String[]{"Buscar", "Cambiar país"};
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//
//        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, mmenuList));
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//        mDrawerToggle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                mDrawerLayout,         /* DrawerLayout object */
//                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
//                R.string.app_name,  /* "open drawer" description */
//                R.string.app_name  /* "close drawer" description */
//        ) {
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//            }
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//            }
//        };
//
//        // Set the drawer toggle as the DrawerListener
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
//        //getActionBar().setHomeButtonEnabled(true);

        //Add search fragment
        if(savedInstanceState==null){

            Fragment sf = SearchFragment.newInstance(Utils.getCurrentCountry(this));
            sf.setRetainInstance(true);
            addDynamicFragment(sf, R.id.layout_main_to_replace);

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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
 //       mDrawerToggle.onConfigurationChanged(newConfig);
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

        if (searchFragment.isVisible()) {


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

        if (inSettings)
        {
            backFromSettingsFragment();
            return;
        }
        super.onBackPressed();
    }

    private void backFromSettingsFragment()
    {
        getFragmentManager().popBackStack();
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

    @Override
    public void getImage(String url) {

        new GetImagesAsyncTask(this, url).execute();

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                TAG_REPLACE = "SEARCH_FRAGMENT";
                if (getFragmentManager().findFragmentByTag("SEARCH_FRAGMENT") != null) {
                    fragment = getFragmentManager().findFragmentByTag("SEARCH_FRAGMENT");
                } else
                    fragment = SearchFragment.newInstance("Argentina");
                    fragment.setRetainInstance(true);

                break;
            case 1:
                fragment = CountrySelectorFragment.newInstance();
                fragment.setRetainInstance(true);
                TAG_REPLACE = "COUNTRY_FRAGMENT";
                break;
        }

        // Insert the fragment by replacing any existing fragment
        replaceDynamicFragment(fragment, R.id.layout_main_to_replace, TAG_REPLACE);

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void onCountrySelected(String country) {
        Fragment fragment = SearchFragment.newInstance(country);
        fragment.setRetainInstance(true);
        replaceDynamicFragment(fragment, R.id.layout_main_to_replace, "SEARCH_FRAGMENT");
    }


}