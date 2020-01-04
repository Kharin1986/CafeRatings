package com.gb.rating.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gb.rating.models.CafeItem;

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
                null,null,null, null, null, null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
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

                listCafe.add(item);
            }
            cursor.close();
        }
        return listCafe;
    }

    public CafeItem readCafeByName(String cafeName) {
        CafeItem item = new CafeItem();
        Cursor cursor = database.query(CafeTable.NAME,
                null,
                CafeTable.Cols.CAFE_NAME + "=?",
                new String[]{cafeName}, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            item.setName(cursor.getString(1));
            item.setImg(cursor.getInt(2)); // тут нужно подумать над типом в базе, нужно сохранить путь к изображению
            item.setType(cursor.getString(2));
            item.setDesc(cursor.getString(3));
            item.setRating(cursor.getInt(4));
            item.setCountry(cursor.getString(5));
            item.setCity(cursor.getString(6));
            item.setStreet(cursor.getString(7));
            item.setHome(cursor.getString(8));
            item.setLoc(cursor.getString(9));
            item.setWTime(cursor.getString(10));

            cursor.close();
        }
        return item;
    }

    public void updateCafeByName(ContentValues cv, String name) {
        database.update(CafeTable.NAME, cv,
                CafeTable.Cols.CAFE_NAME + " = ?",
                new String[]{name});
    }

    @Override
    public void close() throws IOException {
        dbHelper.close();
    }
}