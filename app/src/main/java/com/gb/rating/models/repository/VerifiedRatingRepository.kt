package com.gb.rating.models.repository

import com.gb.rating.models.VerifiedRating
import io.reactivex.Maybe
import io.reactivex.annotations.NonNull

interface VerifiedRatingRepository {

    @NonNull
    fun writeVerifiedRating(@NonNull value: VerifiedRating, @NonNull iMEI: String): io.reactivex.Completable

    @NonNull
    fun retrieveVerifiedRatingList(@NonNull iMEI: String): Maybe<List<VerifiedRating>>
}

