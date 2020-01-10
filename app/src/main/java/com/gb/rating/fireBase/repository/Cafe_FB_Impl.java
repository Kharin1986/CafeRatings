package com.gb.rating.fireBase.repository;

import com.gb.rating.fireBase.dataStore.FrangSierraPlus;
import com.gb.rating.fireBase.models_FireBase.Cafe_FB;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.CafeRepository;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;

public class Cafe_FB_Impl implements CafeRepository {
    private Object anyapi=null;
    private FirebaseDatabase db;

    public Cafe_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    public Completable writeCafe(Cafe_FB cafe_fb) {
        return RxFirebaseDatabase.setValue(db.getReference().child("CafeList").child(cafe_fb.country).child(cafe_fb.city).push(), cafe_fb);
    }

    @Override
    public Completable writeCafe(CafeItem cafe) {
        Cafe_FB cafe_fb = new Cafe_FB(cafe.getName(), cafe.getType(), cafe.getDesc(), cafe.getRating(), cafe.getCountry(), cafe.getCity(), cafe.getStreet()+" "+cafe.getHome());
        return RxFirebaseDatabase.setValue(db.getReference().child("CafeList").child(cafe_fb.country).child(cafe_fb.city).push(), cafe);
    }

    @Override
    public Maybe<List<CafeItem>> retrieveCafeList(@NonNull String country, @NonNull String city) {
        Query query = db.getReference().child("UnverifiedRatingsList").child(country).child(city).orderByChild("rating");
        return FrangSierraPlus.observeSingleValueEvent_CafeItemList(query);


    }
}
