package com.mercadolibre.adapters;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadolibre.activities.MainActivity;
import com.mercadolibre.activities.R;
import com.mercadolibre.dto.Item;
import com.mercadolibre.tasks.GetImageListAsyncTask;
import com.squareup.picasso.Picasso;


public class ItemAdapter extends BaseAdapter {


    private ArrayList<Item> itemList;
    private static LayoutInflater lInflater = null;

    public ItemAdapter(LayoutInflater inflater, ArrayList<Item> itemList) {

          this.itemList = itemList;
          this.lInflater = inflater;
    }

    @Override
    public int getCount() { return itemList.size(); }

    @Override
    public Object getItem(int arg0) { return itemList.get(arg0); }

    @Override
    public long getItemId(int arg0) { return arg0; }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        ContainerView container = null;

        if (arg1 == null){

            arg1 = lInflater.inflate(com.mercadolibre.activities.R.layout.list_item,null);
            container = new ContainerView();
            container.title = (TextView) arg1.findViewById(R.id.title_list);
            container.price = (TextView) arg1.findViewById(R.id.price_list);
            container.id = (TextView) arg1.findViewById(R.id.id_item_list);
            container.pic = (ImageView) arg1.findViewById(R.id.image_list);

            arg1.setTag(container);

        } else

            container = (ContainerView) arg1.getTag();

            Item item = (Item) getItem(arg0);

            container.title.setText(item.getTitle());
            container.price.setText(item.getPrice());
            container.id.setText(item.getId());
            Picasso.with(lInflater.getContext()).load(item.getPicture()).resize(100,100).into(container.pic);

  //      new GetImageListAsyncTask(container.pic).execute(item.getPicture());


        return arg1;
    }

    class ContainerView{

        TextView title;
        TextView price;
        TextView id;
        ImageView pic;

    }
}
