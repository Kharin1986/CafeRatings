package com.gb.rating.googleMapsAPI;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gb.rating.filestore.Mapper;
import com.gb.rating.filestore.Point_FB;
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl;
import com.gb.rating.googleMapsAPI.Nearby.NearbySearch;
import com.gb.rating.googleMapsAPI.Nearby.Result;
import com.gb.rating.models.OurSearchPropertiesValue;
import com.gb.rating.models.SearchUtils;
import com.gb.rating.models.repository.CafeRepository;
import com.gb.rating.models.utils.MainApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdateDatabase {
    private static volatile UpdateDatabase instance;

    private  final String TAG = "UdateDatabase";
    private  final String RADIUS_1000 = "1000";
    private  final String TYPE_BAR = "bar";
    private  final String GOOGLE_API_KEY = "AIzaSyBgQhfZPKjhli7XJonmQdUmLtkRnGpUKnU";
    private  final String RANC_BY = "distance";
    private  final long WAIT_FOR_NEW_REQUEST_TO_API = 61000;

    private  long timeOfLastRequest = 0;
    public static UpdateDatabase getInstance() {
        UpdateDatabase localInstance = instance;
        if (localInstance == null) {
            synchronized (UpdateDatabase.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UpdateDatabase();
                }
            }
        }
        return localInstance;
    }

    private  boolean CheckGooglePlayServices() {
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


    public static boolean doIt() throws InterruptedException {
        OurSearchPropertiesValue ourSearchPropertiesValue = SearchUtils.initialSearchProperties();
        OurSearchPropertiesValue.MyPoint point = ourSearchPropertiesValue.getCenterPoint();

        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                return true;
            }
        };
        try {
            return UpdateDatabase.getInstance().LoadGoogleCafeForPoint(ourSearchPropertiesValue, point, callback);
        } catch (Throwable t) {
            return false;
        }
    }

    public  boolean LoadGoogleCafeForPoint(OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point, Handler.Callback callable) throws InterruptedException {
        Api api = RetrofitInit.newApiInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        CafeRepository repository = new Cafe_FB_Impl(db, null);

        String[] cafeGoogleTypeArray = {SearchUtils.RESTAURANT_GOOGLE, SearchUtils.BAR_GOOGLE, SearchUtils.CAFE_GOOGLE};
        Handler h = new Handler(callable);
        for (String cafeGoogleType : cafeGoogleTypeArray
        ) {
            try {
                getNearbySearch(api, repository, ourSearchPropertiesValue, point, null, cafeGoogleType, h);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private  void getNearbySearch(Api api, CafeRepository repository, OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point, String pageToken, String cafeGoogleType, Handler h) throws InterruptedException {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                (pageToken == null ? api.getNearbySearch(point.getLatitude() + "," + point.getLongityde(), cafeGoogleType, GOOGLE_API_KEY, RANC_BY) : api.getNearbySearch(pageToken, GOOGLE_API_KEY)) //TYPE_BAR
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Consumer<NearbySearch>() {
                                       @Override
                                       public void accept(NearbySearch nearbySearch) throws Exception {
                                           Log.d(TAG, "getNearbySearch() SUCCESS: " + nearbySearch);
                                           if (nearbySearch.getStatus().equals("OK")) {
                                               writeToDatabase(nearbySearch, repository, ourSearchPropertiesValue, point, cafeGoogleType);
                                               if (nearbySearch.getNextPageToken() != null) {
                                                   getNearbySearch(api, repository, ourSearchPropertiesValue, point, nearbySearch.getNextPageToken(), cafeGoogleType, h);
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
        };

        long timeOfNewRequest = new Date().getTime();
        long delay;
        if (timeOfLastRequest == 0) {
            timeOfLastRequest = timeOfNewRequest;
        } else {
            timeOfLastRequest += WAIT_FOR_NEW_REQUEST_TO_API;
        }
        delay = Math.max(0,timeOfLastRequest - timeOfNewRequest);
        h.postDelayed(r, delay);

    }

    private  void writeToDatabase(NearbySearch nearbySearch, CafeRepository repository, OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point, String cafeGoogleType) {
        double[] maxCafeDistance = {0.0};
        for (Result curCafe : nearbySearch.getResults()) {
            maxCafeDistance[0] = Math.max(
                    maxCafeDistance[0], geoDistance(point, curCafe));

            repository.writeCafe(com.gb.rating.fireBase_RealTime.models_FireBase.Mapper.convert(curCafe, ourSearchPropertiesValue))
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
        writePoint(point, maxCafeDistance[0], ourSearchPropertiesValue, cafeGoogleType);
    }

    private  void writePoint(OurSearchPropertiesValue.MyPoint point, double radius, OurSearchPropertiesValue ourSearchPropertiesValue, String cafeGoogleType) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Point_FB point_FB = Mapper.convert(point, radius, ourSearchPropertiesValue, cafeGoogleType);
        db.collection("Countries").document(point_FB.country).collection("Cities").document(point_FB.city)
                .collection("Types").document(point_FB.type.equals("") ? "no_type" : point_FB.type)
                .collection("Points").document(point_FB.name()).set(point_FB) //, SetOptions.merge()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("SETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETSETv");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_FAILURE_");
            }
        });

    }


    public  double geoDistance(OurSearchPropertiesValue.MyPoint point, Result curCafe) {
        return Math.sqrt(
                Math.pow(curCafe.getGeometry().getLocation().getLat() - point.getLatitude(), 2) + Math.pow(curCafe.getGeometry().getLocation().getLng() - point.getLongityde(), 2)
        );
    }

}
