package com.elhefny.pixabay.TargetImages;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface jsonInterface {
    @GET("api")
    Call<Images> getTargetImages(@Query("key") String myKey,
                                 @Query("q") String searchQuery,
                                 @Query("page") int pageNumber,
                                 @Query("per_page") int numberOfImagesPerPage,
                                 @Query("order") String order);
}
