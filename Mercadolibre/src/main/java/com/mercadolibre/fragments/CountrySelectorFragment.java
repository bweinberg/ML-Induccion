package com.mercadolibre.fragments;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mercadolibre.activities.R;
import java.util.Arrays;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CountrySelectorFragment extends ListFragment {

    private static TextView headerTv;


    public interface CountrySelectorListener {
        void onCountrySelected(String query);

    }

    private CountrySelectorListener myListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        myListener = (CountrySelectorListener) getActivity();
        super.onActivityCreated(savedInstanceState);

    }

    public static Fragment newInstance() {
        CountrySelectorFragment mFrgment = new CountrySelectorFragment();
        return mFrgment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_country_selector, null);
        Resources res = getResources();
        String[] planets = res.getStringArray(R.array.array_country);
        List<String> countryList = Arrays.asList(planets);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_country_selector, R.id.country, countryList);
        setListAdapter(adapter);
        return view;

    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.preferred_country), ((TextView) view.findViewById(R.id.country)).getText().toString());
                editor.commit();
                myListener.onCountrySelected(((TextView) view.findViewById(R.id.country)).getText().toString());

            }
        });
    }


}