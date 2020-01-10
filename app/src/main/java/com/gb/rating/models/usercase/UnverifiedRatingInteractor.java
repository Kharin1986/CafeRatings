package com.gb.rating.models.usercase;

import com.gb.rating.fireBase.models_FireBase.UnverifiedRatings_FB;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.CafeRepository;
import com.gb.rating.models.repository.UnverifiedRatingRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class UnverifiedRatingInteractor {

    private UnverifiedRatingRepository repository;

    public UnverifiedRatingInteractor(UnverifiedRatingRepository repository) {
        this.repository = repository;
    }

    public Completable writeRating(Object value, String iMEI) {
        String imei = iMEI.equals("")? "NO_IMEI" : iMEI;
        return repository.writeUnverifiedRating(value, imei);
    }

    public Maybe<List<UnverifiedRatings_FB>> retrieveUnverifiedRatingsList(String iMEI) {
        return repository.retrieveUnverifiedRatingList(iMEI);
    }


}
