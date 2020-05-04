package com.gb.rating.dataRoom;

import com.gb.rating.models.utils.PointRepository;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class Point_Room_Impl implements PointRepository<Point_Room, RoomApp> {
    private RoomApp db;
    private PointDao pointDao;

    //----------------------------------------------------------------------------------------------------------------------------------
    //SERVICE METHODS
    public Point_Room_Impl(RoomApp db) {
        this.db = db;
        this.pointDao = this.db.pointDao();
    }

    @Override
    public RoomApp getDB() {
        return db;
    }

    @NotNull
    @Override
    public Completable writePoint(@NotNull Point_Room value) {
        return null;
    }

    @Override
    public Maybe<List<Point_Room>> retrieveAllPoints() {
        return pointDao.getPoints();
    }

    @NotNull
    @Override
    public Maybe<List<Point_Room>> retrievePoints(@NotNull String country, @NotNull String city, @NotNull String googleType, double latitudeFrom, double latitudeTo, double longitudeFrom, double longitudeTo, String deleted) {
        return pointDao.getPoints(country, city, googleType, latitudeFrom, latitudeTo, longitudeFrom, longitudeTo, deleted);
    }

    @Override
    public Maybe<List<Point_Room>> retrieveNewPoints(@NotNull String country, @NotNull String city, @NotNull String cafeGoogleType, long changeTime) {
        return null;
    }


    @NotNull
    @Override
    public long[] writePoints(@NotNull List<Point_Room> points_rooms) {
        long[] result = pointDao.insertPoints(points_rooms);
        return result;

    }


}
