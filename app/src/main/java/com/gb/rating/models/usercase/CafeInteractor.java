package com.gb.rating.models.usercase;

import com.gb.rating.fireBase_RealTime.models_FireBase.Cafe_FB;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.CafeRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public class CafeInteractor {

    private CafeRepository repository;

    public CafeInteractor(CafeRepository repository) {
        this.repository = repository;
    }

    public Completable writeCafe(CafeItem value) throws IllegalArgumentException {
        return repository.writeCafe((CafeItem) value);
    }


    public Maybe<List<CafeItem>> retrieveCafeList(String country, String city) {
        return repository.retrieveCafeList(country, city);
    }

    public Maybe<List<CafeItem>> retrieveCafeListByType(String country, String city, String type) {
        return repository.retrieveCafeListByType(country, city, type);
    }



}
