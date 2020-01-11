package com.gb.rating.ui.fireBaseDemo.ui;

import com.gb.rating.fireBase.models_FireBase.Cafe_FB;
import com.gb.rating.fireBase.models_FireBase.VerifiedRating_FB;
import com.gb.rating.fireBase.repository.UnverifiedRating_FB_Impl;
import com.gb.rating.fireBase.repository.VerifiedRating_FB_Impl;
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

import com.gb.rating.fireBase.models_FireBase.UnverifiedRating_FB;
import com.gb.rating.fireBase.repository.Cafe_FB_Impl;
import com.gb.rating.models.usercase.UnverifiedRatingInteractor;
import com.gb.rating.models.usercase.VerifiedRatingInteractor;
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
    private MutableLiveData<List<UnverifiedRating_FB>> unverifiedRatingsList = new MutableLiveData<List<UnverifiedRating_FB>>();
    private MutableLiveData<List<VerifiedRating_FB>> verifiedRatingsList = new MutableLiveData<>();
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
        TestWriting();
    }

    //TEST
    private void TestWriting() {
        //список Кафе
        Cafe_FB curCafe=new Cafe_FB();
        curCafe.type = "фастфуд";

        curCafe.city="Новосибирск";
        writeCafe(curCafe);

        curCafe.city="Ревда";
        writeCafe(curCafe);

        curCafe.rating = -4;
        writeCafe(curCafe);


        retrieveCafeList("Russian Federations", "Ревда");
        retrieveCafeListByType("Russian Federations", "Ревда", "фастфуд");

        //список неверифицированных отзывов
        UnverifiedRating_FB Unverifiedratings_fb = new UnverifiedRating_FB();
        Unverifiedratings_fb.comment="Неверифицированный отзыв";
        writeUnverifiedRating(Unverifiedratings_fb);

        retrieveUnverifiedRatingsList();

        //список верифицированных отзывов
        UnverifiedRating_FB Verifiedratings_fb = new UnverifiedRating_FB();
        Verifiedratings_fb.comment="Верифицированный отзыв";
        writeVerifiedRating(Verifiedratings_fb);

        retrieveVerifiedRatingsList();


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
    // BASIC ACTIONS

    public void writeUnverifiedRating(Object value){
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

    public void writeVerifiedRating(Object value){
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

    public void writeCafe(Object value){
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
                .subscribe(new MaybeObserver<List<UnverifiedRating_FB>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<UnverifiedRating_FB> ratingList) {
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
                .subscribe(new MaybeObserver<List<VerifiedRating_FB>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<VerifiedRating_FB> ratingList) {
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
