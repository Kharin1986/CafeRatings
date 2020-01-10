package com.gb.rating.models.repository;

import com.gb.rating.models.CafeItem;

import java.util.List;

import io.reactivex.Maybe;

public interface RW {

    public io.reactivex.Completable writeRating(Object value, String iMEI);
    public io.reactivex.Completable writeCafe(Object value);
    public Maybe<List<CafeItem>> retrieveCafeList(String country, String city);

}
