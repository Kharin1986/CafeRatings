package com.gb.rating.models.usercase;

import androidx.annotation.NonNull;

import com.gb.rating.models.UnverifiedRating;
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions;
import com.gb.rating.models.repository.UnverifiedRatingRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class UnverifiedRatingInteractor {

    private UnverifiedRatingRepository repository;

    public UnverifiedRatingInteractor(@NonNull UnverifiedRatingRepository repository) {
        this.repository = repository;
    }

    public Completable writeRating(@NonNull UnverifiedRating rating, String iMEI) {
        String imei = iMEI.equals("")? "NO_IMEI" : iMEI;
        rating.uid = CommonAuthFunctions.getUid();

        return repository.writeUnverifiedRating(rating, imei);
    }

    public Maybe<List<UnverifiedRating>> retrieveUnverifiedRatingsList(@NonNull String iMEI) {
        return repository.retrieveUnverifiedRatingList(iMEI);
    }


}
