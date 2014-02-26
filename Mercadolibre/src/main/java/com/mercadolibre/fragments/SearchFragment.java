package com.mercadolibre.fragments;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mercadolibre.activities.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchFragment extends Fragment {

    static private String countrySelected;
    static private String site;
    private static ImageView icon;

    public SearchFragment(String country) {
        countrySelected = country;
    }

    public interface SearchListener {
        void onQuerySelected(String query, String site);
        void addDynamicFragment(Fragment fg, int layout);
        void replaceDynamicFragment(Fragment fg, int layout, String url);
    }

    private SearchListener myListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        myListener = (SearchListener) getActivity();
        super.onActivityCreated(savedInstanceState);
    }


    public static Fragment newInstance(String country) {
        return new SearchFragment(country);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, null);
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.splash_logo);
        setHasOptionsMenu(true);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                myListener.onQuerySelected(((TextView)getActivity().findViewById(R.id.edit_text)).getText().toString(), site);
//
//            }
//        });

        return view;

    }


}