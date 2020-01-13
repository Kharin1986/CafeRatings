package com.gb.rating.fireBase.repository;

import androidx.annotation.NonNull;

import com.gb.rating.fireBase.models_FireBase.VerifiedRating_FB;
import com.gb.rating.models.repository.VerifiedRatingRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

public class VerifiedRating_FB_Impl implements VerifiedRatingRepository {
    private Object anyapi=null;
    private FirebaseDatabase db;

    //----------------------------------------------------------------------------------------------------------------------------------
    //SERVICE METHODS
    public VerifiedRating_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    public static Function FromSnapshotToVerifiedRating_FBFunction = new Function<DataSnapshot, List<VerifiedRating_FB>>() {
        @Override
        public List<VerifiedRating_FB> apply(DataSnapshot dataSnapshot) throws Exception {
            List<VerifiedRating_FB> ratingList= new ArrayList();

            for (DataSnapshot iMEISnapshot: dataSnapshot.getChildren()) {
                for (DataSnapshot ratingSnapshot: iMEISnapshot.getChildren()) {
                    VerifiedRating_FB curRating=ratingSnapshot.getValue(VerifiedRating_FB.class);
                    ratingList.add(curRating.convertToModelEntity());
                }
            }
            return ratingList;
        }
    };


    @io.reactivex.annotations.NonNull
    public static  Maybe<List<VerifiedRating_FB>> observeSingleValueEvent_VerifiedRating_FBList(@NonNull Query query) {
        return RxFirebaseDatabase.observeSingleValueEvent(query).map(FromSnapshotToVerifiedRating_FBFunction);
    }


    //----------------------------------------------------------------------------------------------------------------------------------
    //MAIN METHODS
    @Override
    public Completable writeVerifiedRating(Object value, String iMEI) {
        return RxFirebaseDatabase.setValue(db.getReference().child("VerifiedRatingsList").child(iMEI).push(), value);
    }

    @Override
    public Maybe<List<VerifiedRating_FB>> retrieveVerifiedRatingList(String iMEI) {
        Query query = db.getReference().child("VerifiedRatingsList").child(iMEI)
                .orderByChild("fiscalDate");
        return observeSingleValueEvent_VerifiedRating_FBList(query);
    }

}
