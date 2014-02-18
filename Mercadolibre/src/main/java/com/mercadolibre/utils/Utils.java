package com.mercadolibre.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.squareup.picasso.Picasso;


/**
 * Created by bweinberg on 21/01/14.
 */

public class Utils {
    public static String getJSONString(String url) {
        String jsonString = null;
        HttpURLConnection linkConnection = null;
        try {
            URL linkurl = new URL(url);
            linkConnection = (HttpURLConnection) linkurl.openConnection();
            int responseCode = linkConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream linkinStream = linkConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int j = 0;
                while ((j = linkinStream.read()) != -1) {
                    baos.write(j);
                }
                byte[] data = baos.toByteArray();
                jsonString = new String(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (linkConnection != null) {
                linkConnection.disconnect();
            }
        }
        return jsonString;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

//    public static PictureByteArray getUploadablePicture(Context context, String picture) {
//        PictureByteArray byteArray = null;
//
//        try {
//            Bitmap bitmap = Picasso.with(context)
//                    .load(new File(picture))
//                    .skipMemoryCache()
//                    .resize(MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
//                    .centerInside()
//                    .get();
//
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_IMAGE_COMPRESSION, stream);
//
//            byteArray = new PictureByteArray("image/jpeg", stream.toByteArray());
//            bitmap.recycle(); stream.close();
//        } catch (IOException e) {}
//
//        return byteArray;
//    }

}

