package com.mercadolibre.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadolibre.activities.R;
import com.mercadolibre.dto.Item;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDetailFragment extends ListFragment {

    public interface ItemDetailListener {

        void getImage(String url);
    }

    static private ItemDetailListener myListener;

    static private Item itemSelected;
    static private TextView titleTv;
    static private TextView priceTv;
    static private TextView availableTv;
    static private TextView endDateTv;
    static private ImageView picIv;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        myListener = (ItemDetailListener) getActivity();

      //  myListener.getImage(itemSelected.getPicture());

    }


    public ItemDetailFragment(Item item) {
        itemSelected = item;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, null);

        return view;


    }

    public void onViewCreated(View view, Bundle savedInstanceState){


        titleTv = (TextView)view.findViewById(R.id.title);
        titleTv.setText(itemSelected.getTitle());
        priceTv = (TextView)view.findViewById(R.id.price);
        priceTv.setText(itemSelected.getPrice());
        availableTv = (TextView)view.findViewById(R.id.available);
        availableTv.setText(String.valueOf(itemSelected.getAvailable_quantity()));
        endDateTv = (TextView)view.findViewById(R.id.end_date);
        picIv = (ImageView)view.findViewById(R.id.pic);
        Picasso.with(getActivity())
               .load(itemSelected.getPictures().get(0).getUrl())
               .into(picIv);

        //  .resize(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)

        String timeLast = "Finaliza en: ";

        Date formattedDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {

            formattedDate = sdf.parse(itemSelected.getStop_time());
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            timeLast= timeLast+sdf2.format(formattedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        endDateTv.setText(timeLast);

    }


    public static Fragment newInstance(Item item) {
        ItemDetailFragment mFrgment = new ItemDetailFragment(item);
        itemSelected = item;
        return mFrgment;
    }


    public void setImage(Bitmap bitmap){

        picIv.setImageBitmap(bitmap);
    }
}
