package com.gb.rating.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.gb.rating.dataBase.CafeDbShema.CafeTable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cafeBase.db";
    private static final int DB_VERSION = 1;

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
                CafeTable.Cols.HOME  + ", " +
                CafeTable.Cols.LOCATION  + ", " +
                CafeTable.Cols.WORK_TIME  +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
