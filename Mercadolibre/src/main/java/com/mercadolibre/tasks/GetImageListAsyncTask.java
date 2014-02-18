package com.mercadolibre.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.lang.ref.WeakReference;
import android.widget.ImageView;

import java.io.InputStream;

public class GetImageListAsyncTask extends AsyncTask<String, String, Bitmap> {


    private final WeakReference imageViewReference;
    static private String url;


    public GetImageListAsyncTask(ImageView pic) {
       imageViewReference = new WeakReference(pic);

    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        Bitmap mIcon11 = null;
        url = strings[0];

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

        if (imageViewReference != null) {

            ImageView imageView = (ImageView)imageViewReference.get();
            if (imageView != null) {
              if (result != null) {
                   imageView.setImageBitmap(result);
               }
            }

        }
    }

}

