package com.elhefny.pixabay.TargetImages;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String baseURL = "https://pixabay.com/";
    public static final String key = "18283226-5b55a899c1ad84b07bf37997c";
    private static Retrofit instance = null;


    private static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder().
                    baseUrl(baseURL).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return instance;
    }

    public static jsonInterface getService() {
        return getInstance().create(jsonInterface.class);
    }
}
