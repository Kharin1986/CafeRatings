package com.gb.rating.models.repository;

import com.gb.rating.models.VerifiedRating;
import java.util.List;
import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public interface VerifiedRatingRepository {

    @NonNull io.reactivex.Completable writeVerifiedRating(@NonNull VerifiedRating value, @NonNull String iMEI);
    @NonNull Maybe<List<VerifiedRating>> retrieveVerifiedRatingList(@NonNull String iMEI);
}

