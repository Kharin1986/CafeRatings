package com.gb.rating.googleMapsAPI;

import android.util.Log;

import com.gb.rating.googleMapsAPI.Nearby.NearbySearch;
import com.gb.rating.googleMapsAPI.Nearby.Result;
import com.gb.rating.models.OurSearchPropertiesValue;
import com.gb.rating.models.SearchUtils;
import com.gb.rating.models.utils.MainApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UpdateDatabase {
    private static final String TAG="UdateDatabase";
    private static final String RADIUS_1000 = "1000";
    private static final String TYPE_BAR = "bar";
    private static final String GOOGLE_API_KEY = "AIzaSyBgQhfZPKjhli7XJonmQdUmLtkRnGpUKnU";

    private static boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(MainApplication.Companion.applicationContext());
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                Log.d(TAG, "no connection with google API");
            }
            return false;
        }
        return true;
    }

    public static boolean doIt(){
        Api api = RetrofitInit.newApiInstance();
        OurSearchPropertiesValue.MyPoint point = (new OurSearchPropertiesValue()).getCenterPoint();
        api.getNearbySearch(point.getLatitude() + "," + point.getLongityde(), RADIUS_1000, TYPE_BAR, GOOGLE_API_KEY)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<NearbySearch>() {
                    @Override
                    public void accept(NearbySearch nearbySearch) throws Exception {
                        Log.d(TAG, "getNearbySearch() SUCCESS: "+nearbySearch);

                        if (nearbySearch.getStatus().equals("OK")) {

                        }
                    }
                }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.d(TAG, "getNearbySearch() ERROR: "+throwable);
                               }
                           }
                );

        return true;

    }

}
