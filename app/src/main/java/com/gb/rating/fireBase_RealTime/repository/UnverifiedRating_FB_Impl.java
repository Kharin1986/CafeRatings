package com.gb.rating.fireBase_RealTime.repository;

//package
import com.gb.rating.fireBase_RealTime.models_FireBase.UnverifiedRating_FB;
import com.gb.rating.models.repository.UnverifiedRatingRepository;
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

public class UnverifiedRating_FB_Impl implements UnverifiedRatingRepository {
    private Object anyapi=null;
    private FirebaseDatabase db;

    //----------------------------------------------------------------------------------------------------------------------------------
    //SERVICE METHODS
    public UnverifiedRating_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    public static Function FromSnapshotToUnverifiedRating_FBFunction = new Function<DataSnapshot, List<UnverifiedRating_FB>>() {
        @Override
        public List<UnverifiedRating_FB> apply(DataSnapshot dataSnapshot) throws Exception {
            List<UnverifiedRating_FB> ratingList= new ArrayList();

            for (DataSnapshot iMEISnapshot: dataSnapshot.getChildren()) {
                for (DataSnapshot ratingSnapshot: iMEISnapshot.getChildren()) {
                    UnverifiedRating_FB curRating=ratingSnapshot.getValue(UnverifiedRating_FB.class);
                    ratingList.add(curRating.convertToModelEntity());
                }
            }
            return ratingList;
        }
    };


    @io.reactivex.annotations.NonNull
    public static  Maybe<List<UnverifiedRating_FB>> observeSingleValueEvent_UnverifiedRating_FBList(@NonNull Query query) {
        return RxFirebaseDatabase.observeSingleValueEvent(query).map(FromSnapshotToUnverifiedRating_FBFunction);
    }


    //----------------------------------------------------------------------------------------------------------------------------------
    //MAIN METHODS
    @Override
    @io.reactivex.annotations.NonNull
    public Completable writeUnverifiedRating(Object value, String iMEI) {
        return RxFirebaseDatabase.setValue(db.getReference().child("UnverifiedRatingsList").child(iMEI).push(), value);
    }

    @Override
    @io.reactivex.annotations.NonNull
    public Maybe<List<UnverifiedRating_FB>> retrieveUnverifiedRatingList(String iMEI) {
        Query query = db.getReference().child("UnverifiedRatingsList").child(iMEI)
                .orderByChild("fiscalDate");
        return observeSingleValueEvent_UnverifiedRating_FBList(query);
    }

}
