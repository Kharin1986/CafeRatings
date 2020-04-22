package com.gb.rating.models.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.gb.rating.dataRoom.Point_Room;
import com.gb.rating.dataRoom.Point_Room_Impl;
import com.gb.rating.dataRoom.RoomApp;
import com.gb.rating.filestore.Mapper;
import com.gb.rating.filestore.Point_FB;
import com.gb.rating.filestore.Point_FB_Impl;
import com.gb.rating.models.OurSearchPropertiesValue;
import com.gb.rating.models.SearchUtils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class UpdatePoints {
    private static volatile UpdatePoints instance;
    private CompositeDisposable compositeDisposable;

    private Point_FB_Impl point_fb_impl;
    private Point_Room_Impl point_room_impl;
    private long pointsLastTimeUpdate;

    final String TAG = "UpdatePoints";

    public long getPointsLastTimeUpdate() {
        return pointsLastTimeUpdate;
    }


    public UpdatePoints() {
        Context context = MainApplication.Companion.applicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        pointsLastTimeUpdate = prefs.getLong("PointsLastTimeUpdate", 0);
        point_fb_impl = new Point_FB_Impl(FirebaseFirestore.getInstance());
        point_room_impl = new Point_Room_Impl(RoomApp.getInstance(MainApplication.Companion.applicationContext()));
        compositeDisposable = new CompositeDisposable();
    }

    public static UpdatePoints getInstance() {
        UpdatePoints localInstance = instance;
        if (localInstance == null) {
            synchronized (UpdatePoints.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UpdatePoints();
                }
            }
        }
        return localInstance;
    }

    public void initialUpdating() {
        OurSearchPropertiesValue ourSearchPropertiesValue = SearchUtils.initialSearchProperties();
        checkNewPointsAndSave(ourSearchPropertiesValue);
    }

    @SuppressLint("CheckResult")
    public void checkNewPointsAndSave(OurSearchPropertiesValue ourSearchPropertiesValue) {
        //сначала - нужно скачать список новых точек из базы одного ремозитория
        String[] cafeGoogleTypeArray = SearchUtils.getGoogleTypes();
        for (String cafeGoogleType : cafeGoogleTypeArray) {
            newPointsRequest(ourSearchPropertiesValue, cafeGoogleType);
        }
    }

    private void newPointsRequest(OurSearchPropertiesValue ourSearchPropertiesValue, String cafeGoogleType) {
        if (ourSearchPropertiesValue.getCountry() == "" || ourSearchPropertiesValue.getCity() == "") return;

        point_fb_impl.retrieveNewPoints(ourSearchPropertiesValue.getCountry(), ourSearchPropertiesValue.getCity(), cafeGoogleType, pointsLastTimeUpdate)
                .observeOn(Schedulers.io()).subscribe(new MaybeObserver<List<Point_FB>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Point_FB> points_fb) {
                List<Point_Room> points_room = new ArrayList<>();
                for (Point_FB point_fb : points_fb
                ) {
                    Point_Room point_room = Mapper.convert(point_fb);
                    points_room.add(point_room);
                    pointsLastTimeUpdate = Math.max(pointsLastTimeUpdate, point_room.changeTime);
                }
                point_room_impl.writePoints(points_room);
                setprefs();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                //новых точек нет, все хорошо, ничего делать не нужно (работает, проверено)
            }
        });
    }

    public void setprefs(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainApplication.Companion.applicationContext());
        SharedPreferences.Editor editor= prefs.edit();
        editor.putLong("PointsLastTimeUpdate", pointsLastTimeUpdate);
        editor.apply();
    }


//    private Disposable newPointsRequest(OurSearchPropertiesValue ourSearchPropertiesValue, String cafeGoogleType) {
//        return point_fb_impl.retrieveNewPoints(ourSearchPropertiesValue.getCountry(), ourSearchPropertiesValue.getCity(), cafeGoogleType, pointsLastTimeUpdate).subscribe(new Consumer<List<Point_FB>>() {
//            @SuppressLint("CheckResult")
//            @Override
//            public void accept(List<Point_FB> points_fb) throws Exception {
//                List<Point_Room> points_room = new ArrayList<>();
//                for (Point_FB point_fb : points_fb
//                ) {
//                    Point_Room point_room = Mapper.convert(point_fb);
//                    points_room.add(point_room);
//                    pointsLastTimeUpdate = Math.max(pointsLastTimeUpdate, point_room.changeTime);
//                    point_room_impl.writePoints(points_room);
//                }
//
//            }
//        });
//    }

}