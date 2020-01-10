package com.gb.rating.fireBase.repository;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.gb.rating.fireBase.dataStore.FrangSierraPlus;
import com.gb.rating.fireBase.models_FireBase.Cafe_FB;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.RW;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;

public class RW_FB_Impl implements RW {
    private Object anyapi=null;
    private FirebaseDatabase db;

    public RW_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    @Override
    public Completable writeRating(Object value, String iMEI) {
        String imei = iMEI.equals("")? "NO_IMEI" : iMEI;
        return RxFirebaseDatabase.setValue(db.getReference().child("UnverifiedRatingsList").child(imei).push(), value);
    }

    @Override
    public Completable writeCafe(Object value) {

        Cafe_FB cafe_fb = null;

        if (value instanceof Cafe_FB) {
            cafe_fb = (Cafe_FB) value;
        }
        if (value instanceof CafeItem) {
            CafeItem c = (CafeItem) value;
            cafe_fb = new Cafe_FB(c.getName(), c.getType(), c.getDesc(), c.getRating(), c.getCountry(), c.getCity(), c.getStreet()+" "+c.getHome());
        }
        return RxFirebaseDatabase.setValue(db.getReference().child("CafeList").child(cafe_fb.country).child(cafe_fb.city).push(), value);
    }

    @Override
    public Maybe<List<CafeItem>> retrieveCafeList(String country, String city) {

            Query query = db.getReference().child("CafeList").child(country).child(city)
                    .orderByChild("rating");

            return FrangSierraPlus.observeSingleValueEvent(query); //.map(dataSnapshot -> );
    }
}
