package com.gb.rating.ui.fireBaseDemo.ui;

import com.gb.rating.fireBase_RealTime.models_FireBase.Cafe_FB;
import com.gb.rating.fireBase_RealTime.models_FireBase.Mapper;
import com.gb.rating.models.VerifiedRating;
import com.gb.rating.fireBase_RealTime.repository.UnverifiedRating_FB_Impl;
import com.gb.rating.fireBase_RealTime.repository.VerifiedRating_FB_Impl;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.CafeRepository;
import com.gb.rating.models.repository.UnverifiedRatingRepository;
import com.gb.rating.models.repository.VerifiedRatingRepository;
import com.gb.rating.models.usercase.CafeInteractor;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.gb.rating.models.UnverifiedRating;
import com.gb.rating.fireBase_RealTime.repository.Cafe_FB_Impl;
import com.gb.rating.models.usercase.UnverifiedRatingInteractor;
import com.gb.rating.models.usercase.VerifiedRatingInteractor;
import com.gb.rating.ui.list.TempCafeList;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class MainViewModel extends ViewModel implements LifecycleObserver {

    private CafeInteractor cafeInteractor; //текущий Интерактор для работы с репозиторием
    private UnverifiedRatingInteractor unverifiedRatingInteractor; //текущий Интерактор для работы с репозиторием
    private VerifiedRatingInteractor verifiedRatingInteractor; //текущий Интерактор для работы с репозиторием
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<CafeItem>> cafeList = new MutableLiveData<List<CafeItem>>();
    private MutableLiveData<List<UnverifiedRating>> unverifiedRatingsList = new MutableLiveData<List<UnverifiedRating>>();
    private MutableLiveData<List<VerifiedRating>> verifiedRatingsList = new MutableLiveData<>();
    public String iMEI="";


    //______________________________________________________________________________________________________________________________________________________________________
    //БАЗОВЫЕ ФУНКЦЦИИ - сеттеры, @OnLifecycleEvent


    public LiveData<List<CafeItem>> getCafeList() {
        return cafeList;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        initInteractors();
        //TestWriting();
        testRetrieveFirebaseRealtime();
    }



    private void initInteractors() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        CafeRepository repository = new Cafe_FB_Impl(db, null);
        cafeInteractor = new CafeInteractor(repository);

        UnverifiedRatingRepository unverifiedRatingRepository = new UnverifiedRating_FB_Impl(db, null);
        unverifiedRatingInteractor = new UnverifiedRatingInteractor(unverifiedRatingRepository);

        VerifiedRatingRepository verifiedRatingRepository = new VerifiedRating_FB_Impl(db, null);
        verifiedRatingInteractor = new VerifiedRatingInteractor(verifiedRatingRepository);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    //______________________________________________________________________________________________________________________________________________________________________
    // TEST ACTIONS

    public void testUpdateFirebaseRealtime() {
        List<CafeItem> cafeList = TempCafeList.INSTANCE.getCafeList();
        for (CafeItem curCafe : cafeList)
        writeCafe(curCafe);

        retrieveCafeList("Россия", "Москва");
        retrieveCafeListByType("Россия", "Москва", "фастфуд");

        //список неверифицированных отзывов
        UnverifiedRating unverifiedRating = new UnverifiedRating();
        unverifiedRating.comment="Неверифицированный отзыв";
        writeUnverifiedRating(unverifiedRating);

        retrieveUnverifiedRatingsList();

        //список верифицированных отзывов
        VerifiedRating verifiedRating = new VerifiedRating();
        verifiedRating.comment="Верифицированный отзыв";
        writeVerifiedRating(verifiedRating);

        retrieveVerifiedRatingsList();

    }

    public void testRetrieveFirebaseRealtime() {

        retrieveCafeList("Россия", "Москва");
        retrieveCafeListByType("Россия", "Москва", "фастфуд");
        retrieveUnverifiedRatingsList();
        retrieveVerifiedRatingsList();

    }

    //TEST
    private void TestWriting() {
        //список Кафе
        Cafe_FB curCafe=new Cafe_FB();
        curCafe.type = "фастфуд";

        curCafe.city="Новосибирск";
        writeCafe(Mapper.convert(curCafe));

        curCafe.city="Ревда";
        writeCafe(Mapper.convert(curCafe));

        curCafe.rating = -4;
        writeCafe(Mapper.convert(curCafe));


        retrieveCafeList("Russian Federations", "Ревда");
        retrieveCafeListByType("Russian Federations", "Ревда", "фастфуд");

        //список неверифицированных отзывов
        UnverifiedRating unverifiedRating = new UnverifiedRating();
        unverifiedRating.comment="Неверифицированный отзыв";
        writeUnverifiedRating(unverifiedRating);

        retrieveUnverifiedRatingsList();

        //список верифицированных отзывов
        VerifiedRating verifiedRating = new VerifiedRating();
        verifiedRating.comment="Верифицированный отзыв";
        writeVerifiedRating(verifiedRating);

        retrieveVerifiedRatingsList();


    }

    //______________________________________________________________________________________________________________________________________________________________________
    // BASIC ACTIONS

    public void writeUnverifiedRating(UnverifiedRating value){
            unverifiedRatingInteractor.writeRating(value, iMEI)
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onStart() {}

                        @Override
                        public void onError(Throwable error) {error.printStackTrace();}

                        @Override
                        public void onComplete() {
                            System.out.println("Rating written!");
                        }
                    });

    }

    public void writeVerifiedRating(VerifiedRating value){
        verifiedRatingInteractor.writeRating(value, iMEI)
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onStart() {}

                    @Override
                    public void onError(Throwable error) {error.printStackTrace();}

                    @Override
                    public void onComplete() {
                        System.out.println("Rating written!");
                    }
                });

    }

    public void writeCafe(CafeItem value){
        cafeInteractor.writeCafe(value)
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onStart(){}

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Cafe written!");
                    }
                });
    }

    public void retrieveCafeList(String country, String city) {

        cafeInteractor.retrieveCafeList(country, city)
                .subscribe(new MaybeObserver<List<CafeItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<CafeItem> cafeItems) {
                        cafeList.setValue(cafeItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        //empty result
                        cafeList.setValue(new ArrayList<>());
                    }
                });
    }

    public void retrieveCafeListByType(String country, String city, String type) {

        cafeInteractor.retrieveCafeListByType(country, city, type)
                .subscribe(new MaybeObserver<List<CafeItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<CafeItem> cafeItems) {
                        cafeList.setValue(cafeItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        //empty result
                        cafeList.setValue(new ArrayList<>());
                    }
                });
    }


    public void retrieveUnverifiedRatingsList() {

        unverifiedRatingInteractor.retrieveUnverifiedRatingsList(iMEI)
                .subscribe(new MaybeObserver<List<UnverifiedRating>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<UnverifiedRating> ratingList) {
                        unverifiedRatingsList.setValue(ratingList);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        //empty result
                        unverifiedRatingsList.setValue(new ArrayList<>());
                    }
                });
    }

    public void retrieveVerifiedRatingsList() {

        verifiedRatingInteractor.retrieveVerifiedRatingsList(iMEI)
                .subscribe(new MaybeObserver<List<VerifiedRating>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<VerifiedRating> ratingList) {
                        verifiedRatingsList.setValue(ratingList);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        //empty result
                        verifiedRatingsList.setValue(new ArrayList<>());
                    }
                });
    }

}
