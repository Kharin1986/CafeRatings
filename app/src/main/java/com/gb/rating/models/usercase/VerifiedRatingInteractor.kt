package com.gb.rating.models.usercase

//package
import com.gb.rating.models.VerifiedRating
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions
import com.gb.rating.models.repository.VerifiedRatingRepository
import io.reactivex.Completable
import io.reactivex.Maybe//common


class VerifiedRatingInteractor(private val repository: VerifiedRatingRepository) {

    fun writeRating(rating: VerifiedRating, iMEI: String): Completable {
        val imei = if (iMEI == "") "NO_IMEI" else iMEI
        rating.uid = CommonAuthFunctions.getUid()

        return repository.writeVerifiedRating(rating, imei)
    }

    fun retrieveVerifiedRatingsList(iMEI: String): Maybe<List<VerifiedRating>> {
        return repository.retrieveVerifiedRatingList(iMEI)
    }
}
