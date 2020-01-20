package com.gb.rating.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gb.rating.models.CafeItem;
import com.gb.rating.ui.list.TempCafeList;

import java.util.List;

import static com.gb.rating.dataBase.CafeDbScheme.CafeTable;


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
                CafeTable.Cols.HOME + ", " +
                CafeTable.Cols.LOCATION + ", " +
                CafeTable.Cols.WORK_TIME + ", " +
                CafeTable.Cols.CAFE_ID +
                ");"
        );
        db.execSQL("CREATE INDEX "+CafeTable.NAME+"_"+CafeTable.Cols.RATING+"_idx ON "+CafeTable.NAME+"("+CafeTable.Cols.RATING+")");

        //временный код для наполнения БД из класса TempCafeList

//        List<CafeItem> list = TempCafeList.INSTANCE.getCafeList();
//        ContentValues cv = new ContentValues();
//        CafeItem item;
//        for (int i = 0; i < list.size(); i++) {
//            item = list.get(i);
//            cv.put(CafeTable.Cols.CAFE_NAME, item.getName());
//            cv.put(CafeTable.Cols.TYPE, item.getType());
//            cv.put(CafeTable.Cols.DESCRIPTION, item.getDesc());
//            cv.put(CafeTable.Cols.RATING, item.getRating());
//            cv.put(CafeTable.Cols.COUNTRY, item.getCountry());
//            cv.put(CafeTable.Cols.CITY, item.getCity());
//            cv.put(CafeTable.Cols.STREET, item.getStreet());
//            cv.put(CafeTable.Cols.HOME, item.getHome());
//            cv.put(CafeTable.Cols.LOCATION, item.getLoc());
//            cv.put(CafeTable.Cols.WORK_TIME, item.getWTime());
//            cv.put(CafeTable.Cols.WORK_TIME, item.getWTime());
//            cv.put(CafeTable.Cols.CAFE_ID, item.getCafeId());
//            db.insert(CafeTable.NAME, null, cv);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}