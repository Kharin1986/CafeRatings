package com.gb.rating.googleMapsAPI;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gb.rating.filestore.Mapper;
import com.gb.rating.filestore.Point_FB;
import com.gb.rating.filestore.Point_FB_Impl;
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl;
import com.gb.rating.googleMapsAPI.Nearby.NearbySearch;
import com.gb.rating.googleMapsAPI.Nearby.Result;
import com.gb.rating.models.OurSearchPropertiesValue;
import com.gb.rating.models.SearchUtils;
import com.gb.rating.models.repository.CafeRepository;
import com.gb.rating.models.utils.MainApplication;
import com.gb.rating.models.utils.PointRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.BoundingBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class UpdateDatabase {
    private static volatile UpdateDatabase instance;

    private final String TAG = "UdateDatabase";
    private final String RADIUS_1000 = "1000";
    private final String TYPE_BAR = "bar";
    private final String GOOGLE_API_KEY = "AIzaSyBgQhfZPKjhli7XJonmQdUmLtkRnGpUKnU";
    private final String RANC_BY = "distance";
    private final long WAIT_FOR_NEW_REQUEST_TO_API = 61000;
    private final boolean USE_REQUEST_CONTINUATION = true;


    private long timeOfLastRequest = 0;
    private GMAPI gmAPI;
    private CafeRepository repository;
    private PointRepository pointRepository;

    public static UpdateDatabase getInstance() {
        UpdateDatabase localInstance = instance;
        if (localInstance == null) {
            synchronized (UpdateDatabase.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UpdateDatabase();

                    instance.gmAPI = RetrofitInit.newApiInstance();
                    instance.repository = new Cafe_FB_Impl(FirebaseDatabase.getInstance(), null);
                    instance.pointRepository = new Point_FB_Impl(FirebaseFirestore.getInstance());
                }
            }
        }
        return localInstance;
    }

    private boolean CheckGooglePlayServices() {
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
            return UpdateDatabase.getInstance().LoadGoogleCafeForPoint(ourSearchPropertiesValue, ourSearchPropertiesValue.getBoundingBox(), point, callback);
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean LoadGoogleCafeForPoint(OurSearchPropertiesValue ourSearchPropertiesValue, BoundingBox boundingBox, OurSearchPropertiesValue.MyPoint point, Handler.Callback callable) throws InterruptedException {
        String[] cafeGoogleTypeArray = SearchUtils.getGoogleTypes();
        Handler h = new Handler(callable);
        for (String cafeGoogleType : cafeGoogleTypeArray
        ) {
            try {

                if (boundingBox != null) {
                    findEmptySpaceAndLoad(ourSearchPropertiesValue, boundingBox, cafeGoogleType, h);
                } else {
                    getNearbySearch(ourSearchPropertiesValue, point, null, cafeGoogleType, h);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    private void findEmptySpaceAndLoad(OurSearchPropertiesValue ourSearchPropertiesValue, BoundingBox boundingBox, String cafeGoogleType, Handler h) {
        pointRepository.retrievePoints(ourSearchPropertiesValue.getCountry(), ourSearchPropertiesValue.getCity(), cafeGoogleType, boundingBox.getActualSouth(), boundingBox.getActualNorth(), boundingBox.getLonWest(), boundingBox.getLonEast(), String.valueOf(false))
                .subscribe(new MaybeObserver<List<Point_FB>>() {
                               @Override
                               public void onSubscribe(Disposable d) {
                               }

                               @Override
                               public void onSuccess(List<Point_FB> points) {
                                   //TODO - работа с точками

                                   boolean result = countCenterAndLoadCafes(ourSearchPropertiesValue, boundingBox, cafeGoogleType, h, points);
                               }

                               @Override
                               public void onError(Throwable e) {
                               }

                               @Override
                               public void onComplete() {
                                   try {
                                       getNearbySearch(ourSearchPropertiesValue, SearchUtils.boundingBoxToMyPoint(boundingBox), null, cafeGoogleType, h);
                                   } catch (InterruptedException e) {
                                       e.printStackTrace();
                                   }
                               }
                           }

                );
    }

    private boolean countCenterAndLoadCafes(OurSearchPropertiesValue ourSearchPropertiesValue, BoundingBox boundingBox, String cafeGoogleType, Handler h, List<Point_FB> points) {

        OurSearchPropertiesValue.MyPoint centerPoint;

        if (points.size() == 0) {
            //return false;
            centerPoint = SearchUtils.boundingBoxToMyPoint(boundingBox);
        } else {
            //first, middle radius
            double middleRadius = 0;
            for (Point_FB point : points
            ) {
                middleRadius += point.radius;
            }
            middleRadius = middleRadius / points.size();
            double delta = Math.min(middleRadius / 20, boundingBox.getLatitudeSpan()/30);
            if (delta < boundingBox.getLatitudeSpan()/100) {return false;} // ограничение в количестве переборов

            //обнаруживаем точки без кафе
            double middleX = 0;
            double middleY = 0;

            List<OurSearchPropertiesValue.MyPoint> suitablePoints = new ArrayList<>();

            for (double x = boundingBox.getActualSouth(); x <= boundingBox.getActualNorth(); x += delta) {
                for (double y = boundingBox.getLonWest(); y <= boundingBox.getLonEast(); y += delta) {
                    boolean curCovered = false;
                    for (Point_FB point_fb : points) {
                        if (geoDistance(point_fb, x, y) <= point_fb.radius && geoDistance(point_fb, x, y + delta) <= point_fb.radius && geoDistance(point_fb, x + delta, y) <= point_fb.radius
                                && geoDistance(point_fb, x + delta, y + delta) <= point_fb.radius) {
                            curCovered = true;
                        }
                    }
                    if (!curCovered) {
                        middleX += x;
                        middleY += y;
                        suitablePoints.add(new OurSearchPropertiesValue.MyPoint(x, y));
                    }
                }
            }

            if (suitablePoints.size() == 0) {
                return false;
            }

            middleX = middleX / suitablePoints.size();
            middleY = middleY / suitablePoints.size();

            double middleDistance;

            //better zooming
            for (int i = 1; i <= 3; i++) {
                middleDistance = 0;
                for (OurSearchPropertiesValue.MyPoint myPoint:suitablePoints
                ) {
                    middleDistance += geoDistance(myPoint, middleX, middleY);
                }
                middleDistance = middleDistance / suitablePoints.size();

                double middleX2 = 0;
                double middleY2 = 0;
                List<OurSearchPropertiesValue.MyPoint> suitablePoints2 = new ArrayList<>();
                for (OurSearchPropertiesValue.MyPoint myPoint : suitablePoints
                ) {
                    if (geoDistance(myPoint, middleX, middleY) <= 1.01 * middleDistance) {
                        suitablePoints2.add(myPoint);
                        middleX2 += myPoint.getLatitude();
                        middleY2 += myPoint.getLongityde();
                    }
                }
                if (suitablePoints2.size() == 0) {
                    return false;
                }

                //вычисляем среднее для отсеивания
                middleX = middleX2 / suitablePoints2.size();
                middleY = middleY2 / suitablePoints2.size();
                suitablePoints = suitablePoints2; // обнуляем и идем на следующую итерацию
            }

            centerPoint = new OurSearchPropertiesValue.MyPoint(middleX, middleY);
        }

        try {
            getNearbySearch(ourSearchPropertiesValue, centerPoint, null, cafeGoogleType, h);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

//    private void retrievePoints(@NonNull String country, @NonNull String city, String googleType, double latitudeFrom, double latitudeTo, double longitudeFrom, double longitudeTo, Handler h) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Countries").document(country).collection("Cities").document(city)
//                .collection("Types").document(googleType.equals("") ? "no_type" : googleType)
//                .collection("Points").whereGreaterThanOrEqualTo("latitude", latitudeFrom).whereLessThanOrEqualTo("latitude", latitudeTo)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            List<Point_FB> points = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                try {
//                                    Point_FB curPoint = document.toObject(Point_FB.class);
//                                    points.add(curPoint);
//                                } catch (Exception e) {
//                                    Log.d("retrievePoints", "Converting to Point_FB failed: " + document);
//                                }
//
//                            }
//                            h.sendMessage(new Message().setData(points););
//                        } else {
//                            Log.d("retrievePoints", "Request failed: ");
//                            return new ArrayList<Point_FB>();
//                        }
//                    }
//                })
//    }

    private void getNearbySearch(OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point, String pageToken, String cafeGoogleType, Handler h) throws InterruptedException {


        Runnable r = new Runnable() {
            @SuppressLint("CheckResult")
            @Override
            @io.reactivex.annotations.NonNull
            public void run() {
                (pageToken == null ? gmAPI.getNearbySearch(point.getLatitude() + "," + point.getLongityde(), cafeGoogleType, GOOGLE_API_KEY, RANC_BY) : gmAPI.getNearbySearch(pageToken, GOOGLE_API_KEY)) //TYPE_BAR
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Consumer<NearbySearch>() {
                                       @Override
                                       public void accept(NearbySearch nearbySearch) throws Exception {
                                           Log.d(TAG, "getNearbySearch() SUCCESS: " + nearbySearch);
                                           if (nearbySearch.getStatus().equals("OK")) {
                                               writeToDatabase(nearbySearch, repository, ourSearchPropertiesValue, point, cafeGoogleType);
                                               if (USE_REQUEST_CONTINUATION && nearbySearch.getNextPageToken() != null) {
                                                   getNearbySearch(ourSearchPropertiesValue, point, nearbySearch.getNextPageToken(), cafeGoogleType, h);
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
        delay = Math.max(0, timeOfLastRequest - timeOfNewRequest);
        if (delay < 10000) h.postDelayed(r, delay);

    }

    private void writeToDatabase(NearbySearch nearbySearch, CafeRepository repository, OurSearchPropertiesValue ourSearchPropertiesValue, OurSearchPropertiesValue.MyPoint point, String cafeGoogleType) {
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

    private void writePoint(OurSearchPropertiesValue.MyPoint point, double radius, OurSearchPropertiesValue ourSearchPropertiesValue, String cafeGoogleType) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Point_FB point_FB = Mapper.convert(point, radius, ourSearchPropertiesValue, cafeGoogleType);
        db.collection("Countries").document(point_FB.country).collection("Cities").document(point_FB.city)
                .collection("Types").document(point_FB.type.equals("") ? "no_type" : point_FB.type)
                .collection("Points").document(point_FB.name).set(point_FB) //, SetOptions.merge()
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


    private double geoDistance(OurSearchPropertiesValue.MyPoint point, Result curCafe) {
        return Math.sqrt(
                Math.pow(curCafe.getGeometry().getLocation().getLat() - point.getLatitude(), 2) + Math.pow(curCafe.getGeometry().getLocation().getLng() - point.getLongityde(), 2)
        );
    }

    private double geoDistance(OurSearchPropertiesValue.MyPoint point, double x, double y) {
        return Math.sqrt(
                Math.pow(x - point.getLatitude(), 2) + Math.pow(y - point.getLongityde(), 2)
        );
    }

    public double geoDistance(Point_FB point, double x, double y) {
        return Math.sqrt(
                Math.pow(x - point.latitude, 2) + Math.pow(y - point.longitude, 2)
        );
    }

}
