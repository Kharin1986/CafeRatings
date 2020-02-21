
package com.gb.rating.models.usercase

import com.gb.rating.models.UnverifiedRating
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions
import com.gb.rating.models.repository.UnverifiedRatingRepository

import io.reactivex.Completable
import io.reactivex.Maybe

class UnverifiedRatingInteractor(private val repository: UnverifiedRatingRepository) {

    fun writeRating(rating: UnverifiedRating, iMEI: String): Completable {
        val imei = if (iMEI == "") "NO_IMEI" else iMEI
        rating.uid = CommonAuthFunctions.getUid()

        return repository.writeUnverifiedRating(rating, imei)
    }

    fun retrieveUnverifiedRatingsList(iMEI: String): Maybe<List<UnverifiedRating>> {
        return repository.retrieveUnverifiedRatingList(iMEI)
    }


}
