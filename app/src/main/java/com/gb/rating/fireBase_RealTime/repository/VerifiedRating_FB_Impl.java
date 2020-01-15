package com.gb.rating.fireBase_RealTime.repository;

//package
import com.gb.rating.fireBase_RealTime.models_FireBase.Mapper;
import com.gb.rating.models.VerifiedRating;
import com.gb.rating.models.repository.VerifiedRatingRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

//common
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;

//rxJava
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

public class VerifiedRating_FB_Impl implements VerifiedRatingRepository {
    public static final String VERIFIED_RATINGS_LIST_CATALOG = "VerifiedRatingsList";
    public static final String FISCAL_DATE_PROPERTY = "fiscalDate";
    private Object anyapi=null;
    private FirebaseDatabase db;

    //----------------------------------------------------------------------------------------------------------------------------------
    //SERVICE METHODS
    public VerifiedRating_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    public static Function FromSnapshotToVerifiedRating_FBFunction = new Function<DataSnapshot, List<VerifiedRating>>() {
        @Override
        public List<VerifiedRating> apply(DataSnapshot dataSnapshot) throws Exception {
            List<VerifiedRating> ratingList= new ArrayList();

            for (DataSnapshot ratingSnapshot: dataSnapshot.getChildren()) {
                VerifiedRating curRating=ratingSnapshot.getValue(VerifiedRating.class);
                ratingList.add(Mapper.convert(curRating));
            }
            return ratingList;
        }
    };


    @io.reactivex.annotations.NonNull
    public static  Maybe<List<VerifiedRating>> observeSingleValueEvent_VerifiedRating_FBList(@NonNull Query query) {
        return RxFirebaseDatabase.observeSingleValueEvent(query).map(FromSnapshotToVerifiedRating_FBFunction);
    }


    //----------------------------------------------------------------------------------------------------------------------------------
    //MAIN METHODS
    @Override
    public Completable writeVerifiedRating(VerifiedRating rating, String iMEI) {
        return RxFirebaseDatabase.setValue(db.getReference().child(VERIFIED_RATINGS_LIST_CATALOG).child(iMEI).push(), rating);
    }

    @Override
    public Maybe<List<VerifiedRating>> retrieveVerifiedRatingList(String iMEI) {
        Query query = db.getReference().child(VERIFIED_RATINGS_LIST_CATALOG).child(iMEI)
                .orderByChild(FISCAL_DATE_PROPERTY);
        return observeSingleValueEvent_VerifiedRating_FBList(query);
    }

}
