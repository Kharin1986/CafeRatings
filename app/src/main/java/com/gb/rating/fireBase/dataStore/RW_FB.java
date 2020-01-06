package com.gb.rating.fireBase.dataStore;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import durdinapps.rxfirebase2.RxFirebaseDatabase;

public class RW_FB {


    public static io.reactivex.Completable writeRating(FirebaseDatabase db, Object value){

        DatabaseReference ref= db.getReference("UnverifiedRatings_FB");
        return RxFirebaseDatabase.setValue(ref, value);

    }


}
