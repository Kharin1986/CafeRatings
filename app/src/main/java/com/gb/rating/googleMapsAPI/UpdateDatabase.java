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

import java.lang.reflect.Array;
import java.util.ArrayList;

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
        OurSearchPropertiesValue ourSearchPropertiesValue = SearchUtils.initialSearchProperties();
        OurSearchPropertiesValue.MyPoint point = ourSearchPropertiesValue.getCenterPoint();

        return LoadGoogleCafeForPoint(ourSearchPropertiesValue, point);
    }

    public static boolean LoadGoogleCafeForPoint(OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point) {
        Api api = RetrofitInit.newApiInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        CafeRepository repository = new Cafe_FB_Impl(db, null);

        String[] cafeGoogleTypeArray = {SearchUtils.RESTAURANT_GOOGLE, SearchUtils.BAR_GOOGLE, SearchUtils.CAFE_GOOGLE};
        for (String cafeGoogleType : cafeGoogleTypeArray
        ) {
            double PointMaxDistance = getNearbySearch(api, repository, ourSearchPropertiesValue, point, null, cafeGoogleType);
        }

        return true;
    }

    private static void getNearbySearch(Api api, CafeRepository repository, OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point, String pageToken, String cafeGoogleType) {
        (pageToken == null ? api.getNearbySearch(point.getLatitude() + "," + point.getLongityde(), cafeGoogleType, GOOGLE_API_KEY, RANC_BY) : api.getNearbySearch(pageToken, GOOGLE_API_KEY)) //TYPE_BAR
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<NearbySearch>() {
                               @Override
                               public void accept(NearbySearch nearbySearch) throws Exception {
                                   Log.d(TAG, "getNearbySearch() SUCCESS: " + nearbySearch);
                                   if (nearbySearch.getStatus().equals("OK")) {
                                       writeToDatabase(nearbySearch, repository, ourSearchPropertiesValue, point);
                                       if (nearbySearch.getNextPageToken() != null) {
                                           Thread.sleep(61000);
                                           getNearbySearch(api, repository, ourSearchPropertiesValue, point, nearbySearch.getNextPageToken(), cafeGoogleType);
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

    private static void writeToDatabase(NearbySearch nearbySearch, CafeRepository repository, OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point) {
        double maxCafeDistance = 0;
        for (Result curCafe : nearbySearch.getResults()) {
            maxCafeDistance = Math.max(
                    maxCafeDistance, geoDistance(point, curCafe));

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

    public static double geoDistance(OurSearchPropertiesValue.MyPoint point, Result curCafe) {
        return Math.sqrt(
                Math.pow(curCafe.getGeometry().getLocation().getLat() - point.getLatitude(),2) + Math.pow(curCafe.getGeometry().getLocation().getLng() - point.getLongityde(),2)
        );
    }

}
