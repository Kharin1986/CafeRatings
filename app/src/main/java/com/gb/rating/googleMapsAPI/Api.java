package com.gb.rating.googleMapsAPI;


import com.gb.rating.googleMapsAPI.Nearby.NearbySearch;
import com.gb.rating.googleMapsAPI.Nearby.Result;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("place/nearbysearch/json")
    Single<NearbySearch> getNearbySearch(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("type") String types,
            @Query("key") String key);
}
