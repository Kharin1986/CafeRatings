package com.gb.rating.dataRoom;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gb.rating.filestore.Point_FB;

import java.util.List;

import javax.annotation.Nullable;

import io.reactivex.Maybe;

@Dao
abstract class PointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long insertArticle(Point_Room point);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long[] insertPoints(List<Point_Room> points);

    // Update ищет в бд запись по ключу. Если не найдет, то ничего не произойдет.
    // Если найдет, то обновит все поля, а не только те, которые мы заполнили в Entity объекте.
    // В ответ получаем кол-во обновленных записей
    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract int updatePoint(Point_Room point);

    // Так же ищет данные по ключу и удаляет
    // В ответ получаем кол-во удаленных записей
    @Delete
    abstract void deletePoint(Point_Room point);


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    // ПОЛУЧЕНИЕ СПИСКА ТОЧЕК

    @Query("SELECT * FROM points WHERE deleted = 'false'")
    abstract List<Point_Room> getPoints();

    @Query("SELECT * FROM points WHERE (:deleted = '' OR deleted = :deleted) AND country = :country AND city = :city  AND (:type = '' OR type=:type) AND latitudeSouth <= :latitudeNorth AND latitudeNorth >= :latitudeSouth AND longitudeWest >= :longitudeEast AND longitudeEast <= :longitudeWest")
    abstract List<Point_Room> getPoints(@NonNull String country, @NonNull String city, @NonNull String type, double latitudeSouth, double latitudeNorth, double longitudeWest, double longitudeEast, @NonNull String deleted);



}
