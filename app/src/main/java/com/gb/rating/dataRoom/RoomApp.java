package com.gb.rating.dataRoom;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {Point_Room.class},
        version = 1,
        exportSchema = false)
public abstract class RoomApp extends RoomDatabase {

    private static volatile RoomApp INSTANCE;

    public static RoomApp getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RoomApp.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RoomApp.class,
                            "CafeRateRoom.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    abstract PointDao pointDao();
}