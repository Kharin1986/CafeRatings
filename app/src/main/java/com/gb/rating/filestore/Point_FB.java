package com.gb.rating.filestore;

import androidx.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

@IgnoreExtraProperties
public class Point_FB {

    public String name = "no_name";
    public String country = "Россия";
    public String city = "";
    public String type = "";
    public double latitude = 0;
    public double longitude = 0;
    public double radius = 0;
    public long changeTime = new Date().getTime();
    public boolean deleted = false;

    public Point_FB() {
        this.name = name(this);
    }

    public Point_FB(String country, String city, String type, double latitude, double longitude, double radius, long changeTime, boolean deleted) {
        this.country = country;
        this.city = city;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.changeTime = changeTime;
        this.name = name(this);
    }

    public Point_FB(String country, String city, String type, double latitude, double longitude, double radius) {
        this.country = country;
        this.city = city;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.name = name(this);
    }


    @NonNull
    @Exclude
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
    @Exclude
    public String name(Point_FB point_room) {
        java.text.DecimalFormat fmt = new java.text.DecimalFormat();
        fmt.setMaximumFractionDigits(10);
        fmt.setMinimumFractionDigits(10);
        fmt.setGroupingUsed(false);

        String curName =  (""+type+" " + fmt.format(latitude) + " " + fmt.format(latitude)).trim();
        return curName.equals("")? "no_name" : curName;
    }
}

