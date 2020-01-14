package com.gb.rating.models.repository;

import com.gb.rating.fireBase.models_FireBase.UnverifiedRating_FB;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public interface UnverifiedRatingRepository {

    @NonNull io.reactivex.Completable writeUnverifiedRating(@NonNull Object value, @NonNull String iMEI);
    @NonNull Maybe<List<UnverifiedRating_FB>> retrieveUnverifiedRatingList(@NonNull String iMEI);
}

