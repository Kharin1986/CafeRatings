package com.gb.rating.googleMapsAPI;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInit {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    private static com.gb.rating.googleMapsAPI.Api api;

    public static synchronized com.gb.rating.googleMapsAPI.Api newApiInstance() {
        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(com.gb.rating.googleMapsAPI.Api.class);
        }
        return api;
    }
}
