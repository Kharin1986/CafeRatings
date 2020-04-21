package com.gb.rating.models.utils;

import androidx.annotation.NonNull;

import java.util.Date;



public abstract class Point  {
    private String country = "Россия";
    private String city = "";
    private String type = "";
    private double radius = 0;
    private double latitude = 0;
    private long changeTime = new Date().getTime();
    private boolean deleted = false;
    private double longitude = 0;

    public Point() {
    }

    public Point(String country, String city, String type, double latitude, double longitude, double radius, long changeTime, boolean deleted) {
        this.country = country;
        this.city = city;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.changeTime = changeTime;
    }

    public Point(String country, String city, String type, double latitude, double longitude, double radius) {
        this.country = country;
        this.city = city;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }


    @NonNull
    public String toString() {
        return "Point_FB{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", type='" + type + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", changeTime=" + changeTime +
                '}';
    }

    @NonNull
    public String name() {
        java.text.DecimalFormat fmt = new java.text.DecimalFormat();
        fmt.setMaximumFractionDigits(10);
        fmt.setMinimumFractionDigits(10);
        fmt.setGroupingUsed(false);

        return ""+type+" " + fmt.format(latitude) + " " + fmt.format(latitude);
    }
}
