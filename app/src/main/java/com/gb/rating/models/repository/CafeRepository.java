package com.gb.rating.models.repository;

import com.gb.rating.fireBase.models_FireBase.Cafe_FB;
import com.gb.rating.models.CafeItem;
import com.gb.rating.ui.list.TempCafeList;

import java.util.List;
import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public interface CafeRepository {

    @NonNull io.reactivex.Completable writeCafe(@NonNull CafeItem value);
    @NonNull io.reactivex.Completable writeCafe(@NonNull Cafe_FB value);


    @NonNull Maybe<List<CafeItem>> retrieveCafeList(@NonNull String country, @NonNull String city);
    @NonNull Maybe<List<CafeItem>> retrieveCafeListByType(@NonNull String country, @NonNull String city, String type);
}

