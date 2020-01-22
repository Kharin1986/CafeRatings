package com.gb.rating.models.usercase;

import com.gb.rating.dataBase.CafeDataSource;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.CafeRepository;
import com.gb.rating.models.utils.MainApplication;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;

public class CafeInteractor {

    private CafeRepository repository;

    public CafeInteractor(CafeRepository repository) {
        this.repository = repository;
    }

    public Completable writeCafe(CafeItem cafe) throws IllegalArgumentException {
        return repository.writeCafe(cafe);
    }


    public Maybe<List<CafeItem>> retrieveCafeList(String country, String city) {
        return repository.retrieveCafeList(country, city);
    }

    public Maybe<List<CafeItem>> retrieveCafeListByType(String country, String city, String type) {
        return repository.retrieveCafeListByType(country, city, type);
    }



//    public Maybe<List<CafeItem>> updateInternalDatabase(String country, String city) {
//        return repository.retrieveCafeList(country, city)
//                .doOnSuccess(new Consumer<List<CafeItem>>() {
//                    @Override
//                    public void accept(List<CafeItem> cafeItems) throws Exception {
//                        writeRetrievedCafeListToLocalDatabase(cafeItems);
//                    }
//                });
//    }
//
//    private void writeRetrievedCafeListToLocalDatabase(List<CafeItem> cafeItems) {
//        CafeDataSource dbHelperW = CafeDataSource(MainApplication.applicationContext()); //getApplicationContext()
//        dbHelperW.openW();
//        dbHelperW.writeCafeList(cafeItems);
//    }

}
