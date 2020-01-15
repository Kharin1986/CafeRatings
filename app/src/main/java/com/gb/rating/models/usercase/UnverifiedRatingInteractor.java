package com.gb.rating.models.usercase;

import androidx.annotation.NonNull;

import com.gb.rating.fireBase_RealTime.models_FireBase.UnverifiedRating_FB;
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions;
import com.gb.rating.models.repository.UnverifiedRatingRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;

public class UnverifiedRatingInteractor {

    private UnverifiedRatingRepository repository;

    public UnverifiedRatingInteractor(@NonNull UnverifiedRatingRepository repository) {
        this.repository = repository;
    }

    public Completable writeRating(@NonNull Object value, String iMEI) {
        String imei = iMEI.equals("")? "NO_IMEI" : iMEI;

        //на будущее, тип может поменяться
        if (! (value instanceof UnverifiedRating_FB)) {
            throw new IllegalArgumentException();
        }
        UnverifiedRating_FB rating = (UnverifiedRating_FB) value;
        rating.uid = CommonAuthFunctions.getUid();

        return repository.writeUnverifiedRating(rating, imei);
    }

    public Maybe<List<UnverifiedRating_FB>> retrieveUnverifiedRatingsList(@NonNull String iMEI) {
        return repository.retrieveUnverifiedRatingList(iMEI);
    }


}
