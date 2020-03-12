package com.gb.rating.googleMapsAPI;

import android.util.Log;

import com.gb.rating.fireBase_RealTime.models_FireBase.Mapper;
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl;
import com.gb.rating.googleMapsAPI.Nearby.NearbySearch;
import com.gb.rating.googleMapsAPI.Nearby.Result;
import com.gb.rating.models.OurSearchPropertiesValue;
import com.gb.rating.models.SearchUtils;
import com.gb.rating.models.repository.CafeRepository;
import com.gb.rating.models.utils.MainApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdateDatabase {
    private static final String TAG = "UdateDatabase";
    private static final String RADIUS_1000 = "1000";
    private static final String TYPE_BAR = "bar";
    private static final String GOOGLE_API_KEY = "AIzaSyBgQhfZPKjhli7XJonmQdUmLtkRnGpUKnU";
    private static final String RANC_BY = "distance";

    private static boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(MainApplication.Companion.applicationContext());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                Log.d(TAG, "no connection with google API");
            }
            return false;
        }
        return true;
    }


    public static boolean doIt() {
        Api api = RetrofitInit.newApiInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        CafeRepository repository = new Cafe_FB_Impl(db, null);

        OurSearchPropertiesValue ourSearchPropertiesValue = SearchUtils.initialSearchProperties();
        OurSearchPropertiesValue.MyPoint point = ourSearchPropertiesValue.getCenterPoint();
        getNearbySearch(api, repository, ourSearchPropertiesValue, point, null);
        return true;
    }

    private static void getNearbySearch(Api api, CafeRepository repository, OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point, String pageToken) {
        (pageToken == null ? api.getNearbySearch(point.getLatitude() + "," + point.getLongityde(), TYPE_BAR, GOOGLE_API_KEY, RANC_BY) : api.getNearbySearch(pageToken, GOOGLE_API_KEY))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<NearbySearch>() {
                               @Override
                               public void accept(NearbySearch nearbySearch) throws Exception {
                                   Log.d(TAG, "getNearbySearch() SUCCESS: " + nearbySearch);
                                   if (nearbySearch.getStatus().equals("OK")) {
                                       writeToDatabase(nearbySearch, repository, ourSearchPropertiesValue);
                                       if (nearbySearch.getNextPageToken() != null) {
                                           Thread.sleep(61000);
                                           getNearbySearch(api, repository, ourSearchPropertiesValue, point, nearbySearch.getNextPageToken());
                                       }
                                   } else {
                                       Log.d(TAG, "getNearbySearch() UNSUCCESSFUL: " + nearbySearch.getStatus());
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.d(TAG, "getNearbySearch() ERROR: " + throwable);
                               }
                           }
                );
    }

    private static void writeToDatabase(NearbySearch nearbySearch, CafeRepository repository, OurSearchPropertiesValue ourSearchPropertiesValue) {
        for (Result curCafe : nearbySearch.getResults()) {
            repository.writeCafe(Mapper.convert(curCafe, ourSearchPropertiesValue))
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onError(Throwable error) {
                            error.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            System.out.println("Cafe written!");
                        }
                    });
        }
    }

}
