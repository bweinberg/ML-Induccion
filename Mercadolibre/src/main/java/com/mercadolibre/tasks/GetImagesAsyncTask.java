package com.mercadolibre.tasks;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.mercadolibre.activities.AsyncTaskCompleteListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class GetImagesAsyncTask extends AsyncTask<Object, String, Bitmap> {

    // Interface
    AsyncTaskCompleteListener callback;
    static private String url;

    public GetImagesAsyncTask(AsyncTaskCompleteListener listener, String pic) {
        this.callback = listener;
        url = pic;
    }

    @Override
    protected Bitmap doInBackground(Object... objects) {

        Bitmap mIcon11 = null;

        try {

            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        callback.onTaskComplete(result);
    }

}

