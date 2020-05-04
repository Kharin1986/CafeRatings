package com.gb.rating.models.utils;

import androidx.annotation.NonNull;

import java.util.Date;



public interface Point<T> {
    String country = "Россия";
    String city = "";
    String type = "";
    double radius = 0;
    double latitude = 0;
    long changeTime = new Date().getTime();
    boolean deleted = false;
    double longitude = 0;


    @NonNull
    String toString();

    @NonNull
    String name(T t);
}
