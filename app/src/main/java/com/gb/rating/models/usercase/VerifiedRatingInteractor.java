package com.gb.rating.models.usercase;

//package
import com.gb.rating.fireBase_RealTime.models_FireBase.VerifiedRating_FB;
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

    public Completable writeRating(@NonNull Object value, String iMEI) {
        String imei = iMEI.equals("")? "NO_IMEI" : iMEI;

        //на будущее, тип может поменяться
        if (! (value instanceof VerifiedRating_FB)) {
            throw new IllegalArgumentException();
        }
        VerifiedRating_FB rating = (VerifiedRating_FB) value;
        rating.uid = CommonAuthFunctions.getUid();

        return repository.writeVerifiedRating(value, imei);
    }

    public Maybe<List<VerifiedRating_FB>> retrieveVerifiedRatingsList(String iMEI) {
        return repository.retrieveVerifiedRatingList(iMEI);
    }


}
