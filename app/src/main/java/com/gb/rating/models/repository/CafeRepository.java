package com.gb.rating.models.repository;

import com.gb.rating.fireBase_RealTime.models_FireBase.Cafe_FB;
import com.gb.rating.models.CafeItem;

import java.util.List;
import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public interface CafeRepository {

    @NonNull io.reactivex.Completable writeCafe(@NonNull CafeItem value);
    @NonNull io.reactivex.Completable writeCafe(@NonNull Cafe_FB value); //временно, этого быть не должно - Cafe_FB неизмвестна в домене

    @NonNull Maybe<List<CafeItem>> retrieveCafeList(@NonNull String country, @NonNull String city);
    @NonNull Maybe<List<CafeItem>> retrieveCafeListByType(@NonNull String country, @NonNull String city, String type);
}

