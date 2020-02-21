package com.gb.rating.models.repository

import com.gb.rating.models.UnverifiedRating
import io.reactivex.Maybe
import io.reactivex.annotations.NonNull

interface UnverifiedRatingRepository {

    @NonNull
    fun writeUnverifiedRating(@NonNull value: UnverifiedRating, @NonNull iMEI: String): io.reactivex.Completable

    @NonNull
    fun retrieveUnverifiedRatingList(@NonNull iMEI: String): Maybe<List<UnverifiedRating>>
}

