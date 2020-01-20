package com.gb.rating.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gb.rating.models.CafeItem;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.gb.rating.dataBase.CafeDbScheme.CafeTable;

public class CafeDataSource implements Closeable {

    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;

    public CafeDataSource(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void openW() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void openR() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void insertCafe(ContentValues cv) {
        database.insert(CafeTable.NAME, null, cv);
    }

    public List<CafeItem> readAllCafe() {
        List<CafeItem> listCafe = new ArrayList<>();
        Cursor cursor = database.query(CafeTable.NAME,
                null,null,null, null, null, CafeTable.Cols.RATING+" DESC");
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                CafeItem item = convertToCafeItem(cursor);
                listCafe.add(item);
            }
            cursor.close();
        }
        return listCafe;
    }

    public List<CafeItem> readAllCafe_Withquery() {
        List<CafeItem> listCafe = new ArrayList<>();
        String query = "SELECT * FROM "+CafeTable.NAME+" order by "+CafeTable.Cols.RATING+"  DESC;";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                CafeItem item = convertToCafeItem(cursor);
                listCafe.add(item);
            }
            cursor.close();
        }
        return listCafe;
    }


    @NotNull
    private CafeItem convertToCafeItem(Cursor cursor) {
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
        return item;
    }

    public CafeItem readCafeByName(String cafeName) {
        CafeItem item = new CafeItem();
        Cursor cursor = database.query(CafeTable.NAME,
                null,
                CafeTable.Cols.CAFE_NAME + "=?",
                new String[]{cafeName}, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            item = convertToCafeItem(cursor);

            cursor.close();
        }
        return item;
    }



    public void updateCafeByCafeId(ContentValues cv, String cafeId) {
        int res = database.update(CafeTable.NAME, cv,
                CafeTable.Cols.CAFE_ID + " = ?",
                new String[]{cafeId});
        if (res < 1)
            database.insert(CafeTable.NAME,null,cv);
    }

    @Override
    public void close() throws IOException {
        dbHelper.close();
    }

    public void writeCafeList(List<CafeItem> list){

        ContentValues cv = new ContentValues();
        CafeItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            cv.put(CafeTable.Cols.CAFE_NAME, item.getName());
            cv.put(CafeTable.Cols.TYPE, item.getType());
            cv.put(CafeTable.Cols.DESCRIPTION, item.getDesc());
            cv.put(CafeTable.Cols.RATING, item.getRating());
            cv.put(CafeTable.Cols.COUNTRY, item.getCountry());
            cv.put(CafeTable.Cols.CITY, item.getCity());
            cv.put(CafeTable.Cols.STREET, item.getStreet());
            cv.put(CafeTable.Cols.HOME, item.getHome());
            cv.put(CafeTable.Cols.LOCATION, item.getLoc());
            cv.put(CafeTable.Cols.WORK_TIME, item.getWTime());
            cv.put(CafeTable.Cols.CAFE_ID, item.getCafeId());
            updateCafeByCafeId(cv,item.getCafeId());
        }

    }
}