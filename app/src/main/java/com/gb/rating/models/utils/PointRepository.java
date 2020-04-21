package com.gb.rating.models.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public interface PointRepository<GenericPoint> {

    @NonNull
    Completable writePoint(@NotNull GenericPoint value);

    @NonNull
    Maybe<List<GenericPoint>> retrievePoints(@NotNull String country, @NotNull String city, String googleType, double latitudeFrom, double latitudeTo, double longitudeFrom, double longitudeTo, String deleted);

    @NonNull
    Maybe<List<GenericPoint>> retrieveNewPoints(@NotNull String country, @NotNull String city, @NotNull String cafeGoogleType, long changeTime);

    long[] writePoints(List<GenericPoint> points);
}