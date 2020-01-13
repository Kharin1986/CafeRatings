package com.gb.rating.fireBase.repository;

import android.app.Activity;
import android.os.Handler;

import com.gb.rating.R;
import com.gb.rating.fireBase.CommonFunctions;
import com.gb.rating.fireBase.models_FireBase.Cafe_FB;
import com.gb.rating.firebase_storage.StorageStaticFunctions;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.CafeRepository;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Maybe;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.functions.Action;
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
                curCafe.cafeId=CafeSnapshot.getKey();
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

    private Task<Void> writeCafe_prepareTask(Cafe_FB cafe_fb){
        //подготовка переменных
        DatabaseReference newRef = db.getReference().child("CafeList").child(cafe_fb.country).child(cafe_fb.city).push();
        String keySaved = newRef.getKey();
        String fileText = "This is root directory for Cafe: "+cafe_fb.toString();
        byte[] data = fileText.getBytes();

        //первый Task (запись в Cloud Storage)
        UploadTask firstTask = StorageStaticFunctions.getReftoImageCatalog().child(""+keySaved+"/cafeProperties.txt").putBytes(data);
        //второй Task (запись в Realtime Database)
        Task<Void> twoTasks = firstTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>(){
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return newRef.setValue(cafe_fb);
            }
        });

        return twoTasks;
    }


    public Completable writeCafe(Cafe_FB cafe_fb) {

        Callable<Task<Void>> c = new Callable<Task<Void>>() {
            @Override
            public Task<Void> call() throws Exception {
                return writeCafe_prepareTask(cafe_fb);
            }
        };

        return CommonFunctions.compleatableFromCallable(c);
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
