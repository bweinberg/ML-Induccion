package com.mercadolibre.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mercadolibre.activities.R;
import com.mercadolibre.adapters.ItemAdapter;
import com.mercadolibre.dto.Item;

import java.util.ArrayList;

public class ListItemFragment extends Fragment {

    public interface ListItemListener {
        void endOfListReached(int count, String site);
        void onItemSelected(String id);
    }

    static private ListItemListener myListener;

    static private ArrayList<Item> itemArrayList;
    static private ItemAdapter itemAdapter;

    // Boolean to control the loading of more pages
    private static boolean loading;
    private static ProgressBar footerTv;
    private static String totalResult;
    private static String siteId;

    private static TextView headerTv;
    ListView itemsList;
    private static String query;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
          myListener = (ListItemListener) getActivity();
        super.onActivityCreated(savedInstanceState);
    }

    public ListItemFragment(ArrayList<Item> itemList) {
        itemArrayList = itemList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, null);

        itemAdapter = new ItemAdapter(inflater, itemArrayList);
        return view;

    }


    public void onViewCreated(View view, Bundle savedInstanceState){

        itemsList = (ListView) view.findViewById(R.id.items_list);
        itemsList.setAdapter(itemAdapter);
        headerTv = (TextView) view.findViewById(R.id.header);
        headerTv.setText(query + " - " + totalResult + " resultados");


        // Add on click listener to see the fragment_item details
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myListener.onItemSelected(((TextView) view.findViewById(R.id.id_item_list)).getText().toString());

            }
        });

         //      Add on scroll listener to show more items
        itemsList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                // Do nothing the first time
                if(itemArrayList.size()!=0){

                    boolean lastItem = (firstVisibleItem + visibleItemCount == totalItemCount);
                    boolean moreRows = itemArrayList.size() < Integer.parseInt(totalResult);


                    if (!loading && lastItem && moreRows){
                        loading = true;
                        // call the asynctask - interface
                        myListener.endOfListReached(totalItemCount, siteId);


                    }

                }

            }

        });

    }

    public static Fragment newInstance(ArrayList<Item> itemList, String total, String search, String site) {
        siteId = site;
        totalResult = total;
        query = search;
        ListItemFragment mFrgment = new ListItemFragment(itemList);
        return mFrgment;
    }


    public void notifyDataSetChanged(){

        loading = false;
        itemAdapter.notifyDataSetChanged();

    }


}
