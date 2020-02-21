package com.gb.rating.models.repository

import com.gb.rating.fireBase_RealTime.models_FireBase.Cafe_FB
import com.gb.rating.models.CafeItem
import io.reactivex.Maybe
import io.reactivex.annotations.NonNull

interface CafeRepository {

    @NonNull
    fun writeCafe(@NonNull value: CafeItem): io.reactivex.Completable

    @NonNull
    fun writeCafe(@NonNull value: Cafe_FB): io.reactivex.Completable  //временно, этого быть не должно - Cafe_FB неизмвестна в домене

    @NonNull
    fun retrieveCafeList(@NonNull country: String, @NonNull city: String): Maybe<List<CafeItem>>

    @NonNull
    fun retrieveCafeListByType(@NonNull country: String, @NonNull city: String, type: String): Maybe<List<CafeItem>>
}

