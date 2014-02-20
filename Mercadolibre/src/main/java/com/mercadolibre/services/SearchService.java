package com.mercadolibre.services;


import com.mercadolibre.dto.Item;
import com.mercadolibre.dto.Search;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by bweinberg on 18/02/14.
 */

public interface SearchService {

   @GET("/sites/{site}/search")
    void getItems(@Path("site") String site , @Query("q") String query,  Callback<Search> cb);
   @GET("/sites/{site}/search")
    void getMoreItems(@Path("site") String site, @Query("q") String mLastQuery, @Query("offset") int total, Callback<Search> cb);
   @GET("/items/{item}")
    void getItem(@Path("item") String id, Callback<Item> cb);

}