package com.gb.rating.models.repository;

import com.gb.rating.fireBase_RealTime.models_FireBase.Cafe_FB;
import com.gb.rating.models.CafeItem;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public interface CafeRepository {

    @NonNull
    io.reactivex.Completable writeCafe(@NotNull CafeItem value);

    @NonNull
    io.reactivex.Completable writeCafe(@NotNull Cafe_FB value);  //временно, этого быть не должно - Cafe_FB неизмвестна в домене

    @NonNull
    Maybe<List<CafeItem>> retrieveCafeList(@NotNull String country, @NotNull String city);

    @NonNull
    Maybe<List<CafeItem>> retrieveNewCafeList(@NotNull String country, @NotNull String city, long changeTime);

    @NonNull
    Maybe<List<CafeItem>> retrieveCafeListByType(@NotNull String country, @NotNull String city, String type);

}
