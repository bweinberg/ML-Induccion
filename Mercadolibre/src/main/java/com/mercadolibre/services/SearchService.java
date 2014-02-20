package com.mercadolibre.services;


import com.mercadolibre.dto.Search;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by bweinberg on 18/02/14.
 */

public interface SearchService {

   @GET("/sites/{site}/search?q=ipod")
    void getItems(@Path("site") String site , Callback<Search> cb);


}