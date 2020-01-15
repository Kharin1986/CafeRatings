package com.gb.rating.models.repository;

import com.gb.rating.models.UnverifiedRating;
import java.util.List;
import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public interface UnverifiedRatingRepository {

    @NonNull io.reactivex.Completable writeUnverifiedRating(@NonNull UnverifiedRating value, @NonNull String iMEI);
    @NonNull Maybe<List<UnverifiedRating>> retrieveUnverifiedRatingList(@NonNull String iMEI);
}

