package com.mercadolibre.activities;

import android.graphics.Bitmap;

import com.mercadolibre.dto.Item;

import java.util.ArrayList;

public interface AsyncTaskCompleteListener {

    void onTaskComplete(ArrayList<Item> itemList, String total, String site, boolean loadMore);
    void onTaskComplete(Item item);
    void onTaskComplete(Bitmap bitmap);

}