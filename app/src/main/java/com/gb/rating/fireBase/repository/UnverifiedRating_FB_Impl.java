package com.gb.rating.fireBase.repository;

import com.gb.rating.fireBase.dataStore.FrangSierraPlus;
import com.gb.rating.fireBase.models_FireBase.UnverifiedRatings_FB;
import com.gb.rating.models.repository.CafeRepository;
import com.gb.rating.models.repository.UnverifiedRatingRepository;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;

public class UnverifiedRating_FB_Impl implements UnverifiedRatingRepository {
    private Object anyapi=null;
    private FirebaseDatabase db;

    public UnverifiedRating_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    @Override
    public Completable writeUnverifiedRating(Object value, String iMEI) {
        return RxFirebaseDatabase.setValue(db.getReference().child("UnverifiedRatingsList").child(iMEI).push(), value);
    }

    @Override
    public Maybe<List<UnverifiedRatings_FB>> retrieveUnverifiedRatingList(String iMEI) {
        Query query = db.getReference().child("UnverifiedRatingsList").child(iMEI)
                .orderByChild("fiscalDate");
        return FrangSierraPlus.observeSingleValueEvent_UnverifiedRatingsList(query);
    }

}
