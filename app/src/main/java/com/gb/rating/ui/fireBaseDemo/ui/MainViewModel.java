package com.gb.rating.ui.fireBaseDemo.ui;

import android.util.Log;

import com.gb.rating.fireBase.models_FireBase.Cafe_FB;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.RW;
import com.gb.rating.models.usercase.RWInteractor;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.gb.rating.fireBase.models_FireBase.UnverifiedRatings_FB;
import com.gb.rating.fireBase.repository.RW_FB_Impl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class MainViewModel extends ViewModel implements LifecycleObserver {

    private RWInteractor interactor; //текущий Интерактор для работы с репозиторием
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<CafeItem>> cafeList = new MutableLiveData<List<CafeItem>>();


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
        initInteractor();
        TestWriting();


    }

    //TEST
    private void TestWriting() {
        //список Кафе создание
        Cafe_FB curCafe=new Cafe_FB();

        curCafe.city="Новосибирск";
        writeCafe(curCafe);

        curCafe.city="Ревда";
        writeCafe(curCafe);

        //список отзывов создание
        UnverifiedRatings_FB ratings_fb = new UnverifiedRatings_FB();
        ratings_fb.comment="Первый отзыв";
        writeRating(ratings_fb);

        retrieveCafeList("Russian Federations", "Ревда");
    }

    private void initInteractor() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        RW repository = new RW_FB_Impl(db, null);
        interactor = new RWInteractor(repository);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }


    //______________________________________________________________________________________________________________________________________________________________________
    // BASIC ACTIONS

    public void writeRating(Object value){
            interactor.writeRating(value)
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onStart() {}

                        @Override
                        public void onError(Throwable error) {error.printStackTrace();}

                        @Override
                        public void onComplete() {
                            System.out.println("Done!");
                        }
                    });

    }

    public void writeCafe(Object value){
        interactor.writeCafe(value)
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onStart(){}

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });
    }

    public void retrieveCafeList(String country, String city) {

        interactor.retrieveCafeList(country, city)
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
                    }
                });
    }

}
