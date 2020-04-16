package com.gb.rating.googleMapsAPI;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInit {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    private static GMAPI GMAPI;

    public static synchronized GMAPI newApiInstance() {
        if (GMAPI == null) {
            GMAPI = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(GMAPI.class);
        }
        return GMAPI;
    }
}
