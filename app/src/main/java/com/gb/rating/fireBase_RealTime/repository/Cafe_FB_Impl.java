package com.gb.rating.fireBase_RealTime.repository;

//package

import com.gb.rating.fireBase_RealTime.FrangSierraPlus;
import com.gb.rating.fireBase_RealTime.models_FireBase.Cafe_FB;
import com.gb.rating.fireBase_RealTime.models_FireBase.Mapper;
import com.gb.rating.firebase_storage.StorageStaticFunctions;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.repository.CafeRepository;

//common
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import androidx.annotation.NonNull;

//firebase
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.UploadTask;

//rxJava 2.0
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class Cafe_FB_Impl implements CafeRepository {
    public static final String CAFELIST_CATALOG = "CafeList";
    public static final String TYPE_PROPERTY = "type";
    public static final String RATING_PROPERTY = "rating";
    public static final String CAFE_PROPERTIES_FILE_NAME = "cafeProperties.txt";
    private Object anyapi = null;
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
            List<CafeItem> cafeList = new ArrayList();

            for (DataSnapshot CafeSnapshot : dataSnapshot.getChildren()) {
                Cafe_FB curCafe = CafeSnapshot.getValue(Cafe_FB.class);
                curCafe.cafeId = CafeSnapshot.getKey();
                cafeList.add(Mapper.convert(curCafe));
            }
            return cafeList;
        }
    };


    @NonNull
    public static Maybe<List<CafeItem>> observeSingleValueEvent_CafeItemList(@NonNull final Query query) {
        return FrangSierraPlus.observeSingleValueEvent(query).map(FromSnapshotToCafeItemFunction);
    }


    //----------------------------------------------------------------------------------------------------------------------------------
    //MAIN METHODS

    private Task<Void> writeCafe_prepareTask(@NonNull Cafe_FB cafe_fb) {
        //подготовка переменных
        DatabaseReference refToCityCafeList = db.getReference().child(CAFELIST_CATALOG).child(cafe_fb.country).child(cafe_fb.city);

        //cafeID - добавляем новое кафе через .push()
        DatabaseReference newRef = ("".equals(cafe_fb.cafeId)) ? refToCityCafeList.push() : refToCityCafeList.child(cafe_fb.cafeId);
        String cafeId = newRef.getKey();

        String fileText = "This is root directory for Cafe: " + cafe_fb.toString();
        byte[] data = fileText.getBytes();

        //первый Task (запись в Cloud Storage)
        UploadTask firstTask = StorageStaticFunctions.getReftoImageCatalog().child("" + cafeId + "/" + CAFE_PROPERTIES_FILE_NAME).putBytes(data);
        //второй Task (запись в Realtime Database)
        Task<Void> twoTasks = firstTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return newRef.setValue(cafe_fb);
            }
        });

        return twoTasks;
    }


    public Completable writeCafe(@NonNull Cafe_FB cafe_fb) {

        Callable<Task<Void>> c = new Callable<Task<Void>>() {
            @Override
            public Task<Void> call() throws Exception {
                return writeCafe_prepareTask(cafe_fb);
            }
        };

        return FrangSierraPlus.compleatableFromCallable(c);
    }

    @Override
    public Completable writeCafe(@NonNull CafeItem cafe) {
        Cafe_FB cafe_fb = Mapper.convert(cafe);
        return writeCafe(cafe_fb);
    }

    @Override
    public @NonNull
    Maybe<List<CafeItem>> retrieveCafeList(@NonNull String country, @NonNull String city) {
        Query query = db.getReference().child(CAFELIST_CATALOG).child(country).child(city)
                .orderByChild(RATING_PROPERTY);
        return observeSingleValueEvent_CafeItemList(query);
    }


    public static Comparator<CafeItem> comparatorByRatingDescending = new Comparator<CafeItem>() {
        @Override
        public int compare(CafeItem o1, CafeItem o2) {

            int r1 = (o1 == null) ? 0 : o1.getRating();
            int r2 = (o2 == null) ? 0 : o2.getRating();

            return r2 - r1;
        }
    };

    @Override
    public @NonNull
    Maybe<List<CafeItem>> retrieveCafeListByType(@NonNull String country, @NonNull String city, String type) {
        if (type == null || "".equals(type)) {
            return retrieveCafeList(country, city);
        }

        Query query = db.getReference().child(CAFELIST_CATALOG).child(country).child(city).orderByChild(TYPE_PROPERTY).equalTo(type);

        return observeSingleValueEvent_CafeItemList(query).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) //в UI можно переопределить subscribeOn, observeOn
                .map(new Function<List<CafeItem>, List<CafeItem>>() {
                    @Override
                    public List<CafeItem> apply(List<CafeItem> cafeItems) throws Exception {
                        Collections.sort(cafeItems, comparatorByRatingDescending);
                        return cafeItems;
                    }
                })
                ;

    }
}
