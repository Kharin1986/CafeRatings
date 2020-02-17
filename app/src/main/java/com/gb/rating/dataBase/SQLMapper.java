package com.gb.rating.dataBase;

import android.content.ContentValues;
import android.database.Cursor;

import com.gb.rating.models.CafeItem;
import com.gb.rating.models.OurSearchPropertiesValue;
import com.gb.rating.models.SearchUtils;

import org.jetbrains.annotations.NotNull;

public class SQLMapper {

    //портянки
    @NotNull
    public static CafeItem convert(Cursor cursor) {
        CafeItem item = new CafeItem();
        item.setName(cursor.getString(1));
        item.setImg(cursor.getInt(2)); // тут нужно подумать как хранить путь к изображению
        item.setType(cursor.getString(2));
        item.setDesc(cursor.getString(3));
        item.setRating(cursor.getInt(4));
        item.setCountry(cursor.getString(5));
        item.setCity(cursor.getString(6));
        item.setStreet(cursor.getString(7));
        item.setHome(cursor.getString(8));
        item.setLoc(cursor.getString(9));
        item.setWTime(cursor.getString(10));
        item.setCafeId(cursor.getString(11));
        item.setLatitude(cursor.getFloat(12));
        item.setLongitude(cursor.getFloat(13));
        item.setDeleted(cursor.getInt(14) == 1);
        item.setFav(cursor.getInt(17) == 1);
        double dist = SearchUtils.countDistanceToAnotherPoint(new OurSearchPropertiesValue.MyPoint(),new OurSearchPropertiesValue.MyPoint(item.getLatitude(),item.getLongitude()));
        item.setDistance(MyRound(dist));
        return item;
    }

    private static double MyRound(double f){
        return ((double) Math.round(f*10))/10;
    }

    public static ContentValues convert(CafeItem item) {
        ContentValues cv = new ContentValues();
        cv.put(CafeDbScheme.CafeTable.Cols.CAFE_NAME, item.getName());
        cv.put(CafeDbScheme.CafeTable.Cols.TYPE, item.getType());
        cv.put(CafeDbScheme.CafeTable.Cols.DESCRIPTION, item.getDesc());
        cv.put(CafeDbScheme.CafeTable.Cols.RATING, item.getRating());
        cv.put(CafeDbScheme.CafeTable.Cols.COUNTRY, item.getCountry());
        cv.put(CafeDbScheme.CafeTable.Cols.CITY, item.getCity());
        cv.put(CafeDbScheme.CafeTable.Cols.STREET, item.getStreet());
        cv.put(CafeDbScheme.CafeTable.Cols.HOME, item.getHome());
        cv.put(CafeDbScheme.CafeTable.Cols.LOCATION, item.getLoc());
        cv.put(CafeDbScheme.CafeTable.Cols.WORK_TIME, item.getWTime());
        cv.put(CafeDbScheme.CafeTable.Cols.CAFE_ID, item.getCafeId());
        cv.put(CafeDbScheme.CafeTable.Cols.LATITUDE, item.getLatitude());
        cv.put(CafeDbScheme.CafeTable.Cols.LONGITUDE, item.getLongitude());
        cv.put(CafeDbScheme.CafeTable.Cols.DELETED, item.getDeleted());
        return cv;
    }


}
