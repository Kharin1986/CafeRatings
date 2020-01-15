package com.gb.rating.models.usercase;

//package
import com.gb.rating.models.VerifiedRating;
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions;
import com.gb.rating.models.repository.VerifiedRatingRepository;

//common
import androidx.annotation.NonNull;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Maybe;


public class VerifiedRatingInteractor {

    private VerifiedRatingRepository repository;

    public VerifiedRatingInteractor(VerifiedRatingRepository repository) {
        this.repository = repository;
    }

    public Completable writeRating(@NonNull VerifiedRating rating, String iMEI) {
        String imei = iMEI.equals("")? "NO_IMEI" : iMEI;
        rating.uid = CommonAuthFunctions.getUid();

        return repository.writeVerifiedRating(rating, imei);
    }

    public Maybe<List<VerifiedRating>> retrieveVerifiedRatingsList(String iMEI) {
        return repository.retrieveVerifiedRatingList(iMEI);
    }


}
