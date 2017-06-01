package com.example.android.feednews.rest;

import com.example.android.feednews.pojo.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("search")
    Call<NewsModel> getNews(@Query("api-key") String apiKey,
                            @Query("section") String section,
                            @Query("page-size") int pageSize,
                            @Query("show-fields") String showFields,
                            @Query("show-tags") String showTags);
}
