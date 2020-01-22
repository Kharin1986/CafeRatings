package com.gb.rating.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gb.rating.models.CafeItem;
import com.gb.rating.ui.settings.OurSearchPropertiesValue;

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

    public List<CafeItem> readAllCafe(OurSearchPropertiesValue ourSearchPropertiesValue) {
        List<CafeItem> listCafe = new ArrayList<>();
        String selection ="";
        List<String> argList= new ArrayList<>();

        if (! ourSearchPropertiesValue.getType().equals("")){
            selection = selection + (("".equals(selection))? "" : " AND ") + CafeTable.Cols.TYPE + "=?";
            argList.add(ourSearchPropertiesValue.getType());
        }
        String[] args = new String[argList.size()];
        int i = 0;
        for (String arg : argList){
            args[i] = arg;
        }

        Cursor cursor = database.query(CafeTable.NAME,
                null, selection, args, null, null, CafeTable.Cols.RATING+" DESC");
        if (cursor.moveToFirst()) {
            CafeItem item = convertToCafeItem(cursor);
            listCafe.add(item);
            while (cursor.moveToNext()) {
                item = convertToCafeItem(cursor);
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
            CafeItem item = convertToCafeItem(cursor);
            listCafe.add(item);
            while (cursor.moveToNext()) {
                item = convertToCafeItem(cursor);
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
        item.setLatitude(cursor.getInt(12));
        item.setLongitude(cursor.getInt(13));
        item.setDeleted(cursor.getInt(14) == 1);
        return item;
    }

    private void convertFromCafeItem(ContentValues cv, CafeItem item) {
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
        cv.put(CafeTable.Cols.LATITUDE, item.getLatitude());
        cv.put(CafeTable.Cols.LONGITUDE, item.getLongitude());
        cv.put(CafeTable.Cols.DELETED, item.getDeleted());
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
            convertFromCafeItem(cv, item);
            updateCafeByCafeId(cv,item.getCafeId());
        }

    }

}