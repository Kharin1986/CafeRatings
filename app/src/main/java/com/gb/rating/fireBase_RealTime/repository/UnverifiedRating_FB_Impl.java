package com.gb.rating.fireBase_RealTime.repository;

//package

import com.gb.rating.fireBase_RealTime.FrangSierraPlus;
import com.gb.rating.fireBase_RealTime.models_FireBase.Mapper;
import com.gb.rating.models.UnverifiedRating;
import com.gb.rating.models.repository.UnverifiedRatingRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

//common
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;

//rxJava
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

public class UnverifiedRating_FB_Impl implements UnverifiedRatingRepository {
    public static final String UNVERIFIED_RATINGS_LIST_CATALOG = "UnverifiedRatingsList";
    public static final String FISCAL_DATE_PROPERTY = "fiscalDate";
    private Object anyapi = null;
    private FirebaseDatabase db;

    //----------------------------------------------------------------------------------------------------------------------------------
    //SERVICE METHODS
    public UnverifiedRating_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    public static Function FromSnapshotToUnverifiedRating_FBFunction = new Function<DataSnapshot, List<UnverifiedRating>>() {
        @Override
        public List<UnverifiedRating> apply(DataSnapshot dataSnapshot) throws Exception {
            List<UnverifiedRating> ratingList = new ArrayList();

            for (DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                UnverifiedRating curRating = ratingSnapshot.getValue(UnverifiedRating.class);
                ratingList.add(Mapper.convert(curRating));
            }
            return ratingList;
        }
    };


    @io.reactivex.annotations.NonNull
    public static Maybe<List<UnverifiedRating>> observeSingleValueEvent_UnverifiedRating_FBList(@NonNull Query query) {
        return FrangSierraPlus.observeSingleValueEvent(query).map(FromSnapshotToUnverifiedRating_FBFunction);
    }


    //----------------------------------------------------------------------------------------------------------------------------------
    //MAIN METHODS
    @Override
    @io.reactivex.annotations.NonNull
    public Completable writeUnverifiedRating(UnverifiedRating rating, String iMEI) {
        return FrangSierraPlus.setValue(db.getReference().child(UNVERIFIED_RATINGS_LIST_CATALOG).child(iMEI).push(), rating);
    }

    @Override
    @io.reactivex.annotations.NonNull
    public Maybe<List<UnverifiedRating>> retrieveUnverifiedRatingList(String iMEI) {
        Query query = db.getReference().child(UNVERIFIED_RATINGS_LIST_CATALOG).child(iMEI)
                .orderByChild(FISCAL_DATE_PROPERTY);
        return observeSingleValueEvent_UnverifiedRating_FBList(query);
    }

}
