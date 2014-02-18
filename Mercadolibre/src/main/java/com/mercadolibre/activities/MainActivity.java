package com.mercadolibre.activities;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mercadolibre.dto.Item;
import com.mercadolibre.fragments.CountrySelectorFragment;
import com.mercadolibre.fragments.ItemDetailFragment;
import com.mercadolibre.fragments.ListItemFragment;
import com.mercadolibre.fragments.SearchFragment;
import com.mercadolibre.tasks.GetImagesAsyncTask;
import com.mercadolibre.tasks.GetItemDetailAsyncTask;
import com.mercadolibre.tasks.GetItemsAsyncTask;
import java.util.ArrayList;


public class MainActivity extends ListActivity implements AsyncTaskCompleteListener, SearchFragment.SearchListener, ListItemFragment.ListItemListener, ItemDetailFragment.ItemDetailListener, CountrySelectorFragment.CountrySelectorListener {

    private String[] mmenuList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    // Search total. Convert to int.
    private static String total = "";

    // Search in progress
    private static ProgressDialog pDialog;

    // Header with the total search and footer Progress Bar
    private static ProgressBar footerTv;

    private static String mLastQuery;
    private static String TAG_REPLACE = "";


    private ActionBarDrawerToggle mDrawerToggle;

    // Hashmap for ListView
    private static ArrayList<Item> itemList = new ArrayList<Item>();


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        pDialog = new ProgressDialog(this);

        mmenuList = new String[] {"Buscar","Cambiar país"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mmenuList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.abc_ic_go,  /* nav drawer icon to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("MercadoLibre");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Drawer Layout");
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        //Add search fragment
        if(savedInstanceState==null){

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            String countryDefault = sharedPref.getString(getString(R.string.preferred_country), "Argentina");
            Fragment sf = SearchFragment.newInstance(countryDefault);
            sf.setRetainInstance(true);
            addDynamicFragment(sf, R.id.layout_main_to_replace);


        }else{
//
//            Serializable mListInstanceState = savedInstanceState.getSerializable(LIST_INSTANCE_STATE);
//            itemList = (ArrayList) mListInstanceState;
//            footerTv = (ProgressBar) getLayoutInflater().inflate(R.layout.footer, getListView(),false);
//            total = savedInstanceState.getString(TAG_TOTAL);
//
//            headerTv.setText(savedInstanceState.getString(TAG_HEADER));
//            getListView().addFooterView(footerTv);
//
//            adapter = new SimpleAdapter(
//                    MainActivity.this, itemList,
//                    R.layout.list_item, new String[] { TAG_TITLE, TAG_PRICE, TAG_ID
//            }, new int[] { R.id.title_list,
//                    R.id.price_list, R.id.id_item_list });
//            setListAdapter(adapter);
//            getListView().removeFooterView(footerTv);

        }



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addDynamicFragment(Fragment fg, int layout) {
        getFragmentManager().beginTransaction().add(layout, fg, "SEARCH_FRAGMENT").addToBackStack("FRAGMENT_BACKSTACK").commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceDynamicFragment(Fragment fg, int layout, String tag) {
        getFragmentManager().beginTransaction().replace(layout, fg, tag).addToBackStack("FRAGMENT_BACKSTACK").commit();
    }

    @Override
    public void onTaskComplete(ArrayList<Item> loadedList, String totalResults, String site, boolean loadMore) {


//        Update the list with data from the asynctask
        if (pDialog.isShowing())
            pDialog.dismiss();

        if(!loadMore){
            itemList = loadedList;
        }else{
            for(int i=0; i<loadedList.size();i++){
                 itemList.add(loadedList.get(i));
            }
        }

        total = totalResults;


        SearchFragment searchFragment = (SearchFragment) getFragmentManager().findFragmentByTag("SEARCH_FRAGMENT");

        if(searchFragment.isVisible()){


            Fragment lf = ListItemFragment.newInstance(itemList, total, mLastQuery, site);
            lf.setRetainInstance(true);
            replaceDynamicFragment(lf, R.id.layout_main_to_replace, "LIST_FRAGMENT");


        }else {

            ListItemFragment itemFragment = (ListItemFragment) getFragmentManager().findFragmentByTag("LIST_FRAGMENT");

            if(itemFragment.isVisible()){
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

        if(listItemFragment.isVisible()){
            Fragment dif = ItemDetailFragment.newInstance(item);
            dif.setRetainInstance(true);
            replaceDynamicFragment(dif, R.id.layout_main_to_replace, "ITEM_FRAGMENT");
        }

    }


    @Override
    public void onTaskComplete(Bitmap bitmap) {

            ItemDetailFragment itemDetailFragment = (ItemDetailFragment) getFragmentManager().findFragmentByTag("ITEM_FRAGMENT");
            if(itemDetailFragment.isVisible()){
                itemDetailFragment.setImage(bitmap);
            }

    }


    @Override
    public void onQuerySelected(String query, String site) {

        pDialog.setMessage("Cargando...");
        pDialog.show();
        mLastQuery = query;

        new GetItemsAsyncTask(this, mLastQuery, site, false).execute();

    }

    @Override
    public void endOfListReached(int total, String site) {

        pDialog.setMessage("Cargando...");
        pDialog.show();

        new GetItemsAsyncTask(this, mLastQuery, site, total, true).execute();

    }

    @Override
    public void onItemSelected(String id) {

        pDialog.setMessage("Cargando...");
        pDialog.show();
        new GetItemDetailAsyncTask(this, id).execute();

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

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        Fragment fragment = null;

        switch ( position ) {
            case 0:
                TAG_REPLACE = "SEARCH_FRAGMENT";
                if(getFragmentManager().findFragmentByTag("SEARCH_FRAGMENT")!=null){
                    fragment = getFragmentManager().findFragmentByTag("SEARCH_FRAGMENT");
                }else
                    fragment = SearchFragment.newInstance("Argentina");
                break;
            case 1:
                fragment = CountrySelectorFragment.newInstance();
                TAG_REPLACE = "COUNTRY_FRAGMENT";
                break;
        }

        // Insert the fragment by replacing any existing fragment
        replaceDynamicFragment(fragment,R.id.layout_main_to_replace, TAG_REPLACE);

          // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void onCountrySelected(String country) {

        Fragment fragment = SearchFragment.newInstance(country);
        replaceDynamicFragment(fragment, R.id.layout_main_to_replace, "SEARCH_FRAGMENT");


    }


}