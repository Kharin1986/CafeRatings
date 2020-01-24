package com.gb.rating.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gb.rating.models.CafeItem;
import com.gb.rating.ui.list.TempCafeList;
import java.util.List;

import static com.gb.rating.dataBase.CafeDbScheme.CafeTable;
import static com.gb.rating.dataBase.CafeDbScheme.FavCafeTable;



public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cafeBase.db";
    private static final int DB_VERSION = 1;
    private static final String LOCATION_INDEX_NAME = "location";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CafeTable.NAME + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CafeTable.Cols.CAFE_NAME + ", " +
                CafeTable.Cols.TYPE + ", " +
                CafeTable.Cols.DESCRIPTION + ", " +
                CafeTable.Cols.RATING + " INTEGER, " +
                CafeTable.Cols.COUNTRY + ", " +
                CafeTable.Cols.CITY + ", " +
                CafeTable.Cols.STREET + ", " +
                CafeTable.Cols.HOME + ", " +
                CafeTable.Cols.LOCATION + ", " +
                CafeTable.Cols.WORK_TIME + ", " +
                CafeTable.Cols.CAFE_ID + ", " +
                CafeTable.Cols.LATITUDE + " INTEGER, " +
                CafeTable.Cols.LONGITUDE + " INTEGER, " +
                CafeTable.Cols.DELETED + " BOOLEAN " +
                ");"
        );
        db.execSQL("CREATE INDEX "+CafeTable.NAME+"_"+CafeTable.Cols.RATING+"_idx ON "+CafeTable.NAME+"("+CafeTable.Cols.RATING+")");
        db.execSQL("CREATE INDEX "+CafeTable.NAME+"_"+CafeTable.Cols.CAFE_ID+"_idx ON "+CafeTable.NAME+"("+CafeTable.Cols.CAFE_ID+")");
        db.execSQL("CREATE INDEX "+CafeTable.NAME+"_"+LOCATION_INDEX_NAME+"_idx ON "+CafeTable.NAME+"("+CafeTable.Cols.LATITUDE+", "+CafeTable.Cols.LONGITUDE+")");


        db.execSQL("CREATE TABLE " + FavCafeTable.NAME + "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavCafeTable.Cols.CAFE_ID + ", " +
                        FavCafeTable.Cols.FAV + " BOOLEAN " +
                        ");");
        db.execSQL("CREATE INDEX "+FavCafeTable.NAME+"_"+FavCafeTable.Cols.CAFE_ID+"_idx ON "+FavCafeTable.NAME+"("+FavCafeTable.Cols.CAFE_ID+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}