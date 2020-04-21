package com.gb.rating.dataRoom;

import com.gb.rating.models.utils.PointRepository;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class Point_Room_Impl implements PointRepository<Point_Room> {
    private RoomApp db;
    private PointDao pointDao;

    //----------------------------------------------------------------------------------------------------------------------------------
    //SERVICE METHODS
    public Point_Room_Impl(RoomApp db) {
        this.db = db;
        this.pointDao = this.db.pointDao();
    }


    @NotNull
    @Override
    public Completable writePoint(@NotNull Point_Room value) {
        return null;
    }

    @NotNull
    @Override
    public Maybe<List<Point_Room>> retrievePoints(@NotNull String country, @NotNull String city, @NotNull String googleType, double latitudeFrom, double latitudeTo, double longitudeFrom, double longitudeTo, String deleted) {
        return null;
    }

    @Override
    public Maybe<List<Point_Room>> retrieveNewPoints(@NotNull String country, @NotNull String city, @NotNull String cafeGoogleType, long changeTime) {
        return null;
    }


    @NotNull
    @Override
    public long[] writePoints(@NotNull List<Point_Room> points_rooms) {
        return pointDao.insertPoints(points_rooms);

    }
}
