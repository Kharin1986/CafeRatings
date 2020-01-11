package com.gb.rating.models.usercase;

import com.gb.rating.fireBase.models_FireBase.VerifiedRating_FB;
import com.gb.rating.models.repository.VerifiedRatingRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class VerifiedRatingInteractor {

    private VerifiedRatingRepository repository;

    public VerifiedRatingInteractor(VerifiedRatingRepository repository) {
        this.repository = repository;
    }

    public Completable writeRating(Object value, String iMEI) {
        String imei = iMEI.equals("")? "NO_IMEI" : iMEI;
        return repository.writeVerifiedRating(value, imei);
    }

    public Maybe<List<VerifiedRating_FB>> retrieveVerifiedRatingsList(String iMEI) {
        return repository.retrieveVerifiedRatingList(iMEI);
    }


}
