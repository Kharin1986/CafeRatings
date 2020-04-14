package com.gb.rating.filestore;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Point_FB {

    public String country = "Россия";
    public String city = "";
    public String type = "";
    public double latitude = 0;
    public double longitude = 0;
    public double radius = 0;
    public long changeTime = new Date().getTime();

    //CONSTRUCTOR FOR getValue()
    public Point_FB() {
    }

    public Point_FB(String country, String city, String type, double latitude, double longitude, double radius, long changeTime) {
        this.country = country;
        this.city = city;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.changeTime = changeTime;
    }

    public Point_FB(String country, String city, String type, double latitude, double longitude, double radius) {
        this.country = country;
        this.city = city;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    @Exclude
    @Override
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

    public String name() {
        java.text.DecimalFormat fmt = new java.text.DecimalFormat();
        fmt.setMaximumFractionDigits(10);
        fmt.setMinimumFractionDigits(10);
        fmt.setGroupingUsed(false);

        return ""+type+" " + fmt.format(latitude) + " " + fmt.format(latitude);
    }
}
