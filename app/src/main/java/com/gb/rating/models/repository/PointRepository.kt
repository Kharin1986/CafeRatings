package com.gb.rating.models.repository

import com.gb.rating.filestore.Point_FB
import com.gb.rating.models.CafeItem
import io.reactivex.Maybe
import io.reactivex.annotations.NonNull

interface PointRepository {

    @NonNull
    fun writePoint(@NonNull value: Point_FB): io.reactivex.Completable

    @NonNull
    fun retrievePoints(@NonNull country: String, @NonNull city: String, googleType: String, latitudeFrom: Double, latitudeTo: Double, longitudeFrom: Double, longitudeTo: Double): Maybe<List<Point_FB>>

}

