package com.gb.rating.fireBase.repository;

import com.gb.rating.fireBase.dataStore.FrangSierraPlus;
import com.gb.rating.fireBase.models_FireBase.Cafe_FB;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.CafeRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import androidx.annotation.NonNull;
import io.reactivex.functions.Function;

public class Cafe_FB_Impl implements CafeRepository {
    private Object anyapi=null;
    private FirebaseDatabase db;

    //----------------------------------------------------------------------------------------------------------------------------------
    //SERVICE METHODS
    public Cafe_FB_Impl(FirebaseDatabase db, Object api) {
        this.db = db;
        this.anyapi = api;
    }

    public static Function FromSnapshotToCafeItemFunction = new Function<DataSnapshot, List<CafeItem>>() {
        @Override
        public List<CafeItem> apply(DataSnapshot dataSnapshot) throws Exception {
            List<CafeItem> cafeList= new ArrayList();

            for (DataSnapshot CafeSnapshot: dataSnapshot.getChildren()) {
                Cafe_FB curCafe=CafeSnapshot.getValue(Cafe_FB.class);
                cafeList.add(curCafe.convertToModelEntity());
            }
            return cafeList;
        }
    };


    @NonNull
    public static  Maybe<List<CafeItem>> observeSingleValueEvent_CafeItemList(@NonNull final Query query) {
        return RxFirebaseDatabase.observeSingleValueEvent(query).map(FromSnapshotToCafeItemFunction);
    }


    //----------------------------------------------------------------------------------------------------------------------------------
    //MAIN METHODS
    public Completable writeCafe(Cafe_FB cafe_fb) {
        return RxFirebaseDatabase.setValue(db.getReference().child("CafeList").child(cafe_fb.country).child(cafe_fb.city).push(), cafe_fb);
    }

    @Override
    public Completable writeCafe(CafeItem cafe) {
        Cafe_FB cafe_fb = Cafe_FB.convertFromModelEntity(cafe);
        return RxFirebaseDatabase.setValue(db.getReference().child("CafeList").child(cafe_fb.country).child(cafe_fb.city).push(), cafe);
    }

    @Override
    public @NonNull Maybe<List<CafeItem>> retrieveCafeList(@NonNull String country, @NonNull String city) {
        Query query = db.getReference().child("CafeList").child(country).child(city).orderByChild("rating");
        return observeSingleValueEvent_CafeItemList(query);
    }


    public static Comparator<CafeItem> comparatorByRatingDescending = new Comparator<CafeItem>(){
        @Override
        public int compare(CafeItem o1, CafeItem o2) {

            int r1 = (o1 == null) ? 0 : o1.getRating();
            int r2 = (o2 == null) ? 0 : o2.getRating();

            return r2-r1;
        }
    };

    @Override
    public @NonNull Maybe<List<CafeItem>> retrieveCafeListByType(@NonNull String country, @NonNull String city, String type) {
        if (type == null || "".equals(type)) {
            return retrieveCafeList(country, city);
        }

        Query query = db.getReference().child("CafeList").child(country).child(city).orderByChild("type").equalTo(type);

        return observeSingleValueEvent_CafeItemList(query).map(new Function<List<CafeItem>, List<CafeItem>>() {
            @Override
            public List<CafeItem> apply(List<CafeItem> cafeItems) throws Exception {
                Collections.sort(cafeItems, comparatorByRatingDescending);
                return cafeItems;
            }
        });

    }
}
