package com.gb.rating.dataRoom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.gb.rating.models.utils.Point;

import java.util.Date;

@Entity(tableName = "points", indices = {
        @Index(value = {"country", "city", "type"})
})
public class Point_Room implements Point<Point_Room> {

    @PrimaryKey
    @NonNull
    @ColumnInfo.Collate
    public String name = "no_name";

    @ColumnInfo(index = true)
    public String country = "Россия";

    @ColumnInfo(index = true)
    public String city = "Москва";

    @ColumnInfo(index = true)
    public String type = "";

    public double latitude = 0;
    public double longitude = 0;
    public double radius = 0;
    public long changeTime = new Date().getTime();
    public boolean deleted = false;

    @ColumnInfo(index = true)
    public double latitudeSouth = latitude - radius;
    @ColumnInfo(index = true)
    public double latitudeNorth = latitude + radius;
    @ColumnInfo(index = true)
    public double longitudeWest = longitude - radius;
    @ColumnInfo(index = true)
    public double longitudeEast = longitude + radius;

    public Point_Room() {
        setTechnicalFields(this);
    }

    @Ignore
    public Point_Room(String country, String city, String type, double latitude, double longitude, double radius, long changeTime, boolean deleted) {
        this.country = country;
        this.city = city;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.changeTime = changeTime;
        setTechnicalFields(this);
    }

    @Ignore
    public Point_Room(String country, String city, String type, double latitude, double longitude, double radius) {
        this.country = country;
        this.city = city;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        setTechnicalFields(this);
    }

    @Ignore
    public void setTechnicalFields(Point_Room point_room){
        point_room.latitudeSouth = point_room.latitude - point_room.radius;
        point_room.latitudeNorth = point_room.latitude + point_room.radius;
        point_room.longitudeWest = point_room.longitude - point_room.radius;
        point_room.longitudeEast = point_room.longitude + point_room.radius;
        point_room.name = name(this);

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
    public String name(Point_Room point_room) {
        java.text.DecimalFormat fmt = new java.text.DecimalFormat();
        fmt.setMaximumFractionDigits(10);
        fmt.setMinimumFractionDigits(10);
        fmt.setGroupingUsed(false);

        String curName =  (""+type+" " + fmt.format(latitude) + " " + fmt.format(latitude)).trim();
        return curName.equals("")? "no_name" : curName;
    }

}
