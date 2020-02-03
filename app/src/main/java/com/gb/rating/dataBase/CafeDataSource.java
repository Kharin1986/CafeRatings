package com.gb.rating.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gb.rating.models.CafeItem;
import com.gb.rating.ui.settings.OurSearchPropertiesValue;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.gb.rating.dataBase.CafeDbScheme.CafeTable;
import static com.gb.rating.dataBase.CafeDbScheme.FavCafeTable;


public class CafeDataSource implements Closeable {

    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;

    //ADDITIONAL FUNCTIONS
    public CafeDataSource(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void openW() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void openR() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }


    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //READ FUNCTIONS
    //основная выборка - список кафе из базы, ограниченных с помощью OurSearchPropertiesValue
    public List<CafeItem> readAllCafe(OurSearchPropertiesValue ourSearchPropertiesValue) {
        List<CafeItem> listCafe = new ArrayList<>();
        PrepareSelection prepareSelection = new PrepareSelection(ourSearchPropertiesValue).invoke();

        Cursor cursor = database.rawQuery("SELECT * FROM " + CafeTable.NAME
                + " LEFT JOIN " + FavCafeTable.NAME + " ON  " + CafeTable.NAME + "." + CafeTable.Cols.CAFE_ID + " = " + FavCafeTable.NAME + "." + FavCafeTable.Cols.CAFE_ID
                + " WHERE " + prepareSelection.selection + " ORDER BY " + CafeTable.Cols.RATING + " DESC", prepareSelection.args);
        if (cursor.moveToFirst()) {
            CafeItem item = SQLMapper.convert(cursor);
            listCafe.add(item);
            while (cursor.moveToNext()) {
                item = SQLMapper.convert(cursor);
                listCafe.add(item);
            }
            cursor.close();
        }
        return listCafe;
    }

    private class PrepareSelection {
        private OurSearchPropertiesValue ourSearchPropertiesValue;
        String selection;
        String[] args;

        PrepareSelection(OurSearchPropertiesValue ourSearchPropertiesValue) {
            this.ourSearchPropertiesValue = ourSearchPropertiesValue;
        }

        PrepareSelection invoke() {
            ourSearchPropertiesValue.checkLocationFilter();
            selection = " deleted != 1";
            List<String> argList = new ArrayList<>();

            if (!ourSearchPropertiesValue.getType().equals("")) {
                selection += " AND " + CafeTable.Cols.TYPE + "=?";
                argList.add(ourSearchPropertiesValue.getType());
            }

            for (OurSearchPropertiesValue.MyFilter curF : ourSearchPropertiesValue.getOtherFilters()) {
                selection += " AND " + curF.getWhere();
                if (curF.getValue_1() != null) argList.add(curF.getValue_1().toString());
                if (curF.getValue_2() != null) argList.add(curF.getValue_2().toString());
            }


            args = new String[argList.size()];
            int i = 0;
            for (String arg : argList) {
                args[i] = arg;
            }
            return this;
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //WRITE (UPDATE, INSERT, DELETE) FUNCTIONS
    private void updateCafeByCafeId(ContentValues cv, String cafeId) {

        int res = database.update(CafeTable.NAME, cv,
                CafeTable.Cols.CAFE_ID + " = ?",
                new String[]{cafeId});
        if (res < 1)
            database.insert(CafeTable.NAME, null, cv);
    }

    @Override
    public void close() throws IOException {
        dbHelper.close();
    }

    public void writeCafeList(List<CafeItem> list) {

        CafeItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            updateCafeByCafeId(SQLMapper.convert(item), item.getCafeId());
        }
    }

    public void setCafeFav(CafeItem item, Boolean fav) {
        ContentValues cv = new ContentValues();
        cv.put(FavCafeTable.Cols.CAFE_ID, item.getCafeId());
        cv.put(FavCafeTable.Cols.FAV, fav);
        int res = database.update(FavCafeTable.NAME, cv,
                FavCafeTable.Cols.CAFE_ID + " = ?",
                new String[]{item.getCafeId()});
        if (res < 1)
            database.insert(FavCafeTable.NAME, null, cv);
    }

    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        database.delete(CafeTable.NAME, null, null);
        database.delete(FavCafeTable.NAME, null, null);
    }

}