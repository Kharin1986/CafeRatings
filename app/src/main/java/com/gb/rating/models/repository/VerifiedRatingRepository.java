package com.gb.rating.models.repository;

import com.gb.rating.fireBase.models_FireBase.VerifiedRating_FB;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public interface VerifiedRatingRepository {

    @NonNull io.reactivex.Completable writeVerifiedRating(@NonNull Object value, @NonNull String iMEI);
    @NonNull Maybe<List<VerifiedRating_FB>> retrieveVerifiedRatingList(@NonNull String iMEI);
}

