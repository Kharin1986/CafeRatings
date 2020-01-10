package com.gb.rating.fireBase.repository;

import com.gb.rating.fireBase.dataStore.FrangSierraPlus;
import com.gb.rating.fireBase.models_FireBase.VerifiedRatings_FB;
import com.gb.rating.models.repository.VerifiedRatingRepository;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;

public class VerifiedRating_FB_Impl implements VerifiedRatingRepository {
    private Object anyapi=null;
    private FirebaseDatabase db;

    public VerifiedRating_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    @Override
    public Completable writeVerifiedRating(Object value, String iMEI) {
        return RxFirebaseDatabase.setValue(db.getReference().child("VerifiedRatingsList").child(iMEI).push(), value);
    }

    @Override
    public Maybe<List<VerifiedRatings_FB>> retrieveVerifiedRatingList(String iMEI) {
        Query query = db.getReference().child("VerifiedRatingsList").child(iMEI)
                .orderByChild("fiscalDate");
        return FrangSierraPlus.observeSingleValueEvent_VerifiedRatingsList(query);
    }

}
