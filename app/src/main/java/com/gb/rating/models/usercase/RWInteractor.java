package com.gb.rating.models.usercase;

import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.RW;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class RWInteractor {

    private RW repository;

    public RWInteractor(RW repository) {
        this.repository = repository;
    }

    public Completable writeRating(Object value) {
        return repository.writeRating(value);
    }

    public Completable writeCafe(Object value) {
        return repository.writeCafe(value);
    }

    public Maybe<List<CafeItem>> retrieveCafeList(String country, String city) {
        return repository.retrieveCafeList(country, city);
    }


}
