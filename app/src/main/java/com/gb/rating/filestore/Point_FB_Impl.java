package com.gb.rating.filestore;

import android.util.Log;
import androidx.annotation.NonNull;

import com.gb.rating.models.utils.PointRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class Point_FB_Impl implements PointRepository<Point_FB> {
    private FirebaseFirestore db;

    //----------------------------------------------------------------------------------------------------------------------------------
    //SERVICE METHODS
    public Point_FB_Impl(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     * Reads the collection referenced by this DocumentReference
     *
     * @param ref The given Collection reference.
     */
    @NonNull
    private static Maybe<List<Point_FB>> getCollection(@NonNull final Query ref, double latitudeFrom, double latitudeTo, double longitudeFrom, double longitudeTo, String deleted) {
        return Maybe.create(new MaybeOnSubscribe<List<Point_FB>>() {

            @Override
            public void subscribe(MaybeEmitter<List<Point_FB>> emitter) throws Exception {
                ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            emitter.onComplete();
                        } else {
                            List<Point_FB> points = new ArrayList<>();
                            for (QueryDocumentSnapshot document : documentSnapshots) {
                                try {
                                    Point_FB curPoint = document.toObject(Point_FB.class);
                                    if (!(deleted.equals("") || String.valueOf(curPoint.deleted).equals(deleted))) continue;
                                    if (longitudeFrom != 0 && longitudeTo != 0 && latitudeFrom != 0 && latitudeTo == 0){
                                        if (!(curPoint.longitude+curPoint.radius >= longitudeFrom && curPoint.longitude-curPoint.radius <= longitudeTo
                                                && curPoint.latitude+curPoint.radius >= latitudeFrom && curPoint.latitude-curPoint.radius <= latitudeTo && (deleted.equals("")))) {
                                            continue;
                                        }
                                    }

                                    points.add(curPoint);
                                } catch (Exception e) {
                                    Log.d("retrievePoints", "Converting to Point_FB failed: " + document);
                                }

                            }
                            emitter.onSuccess(points);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (!emitter.isDisposed())
                            emitter.onError(e);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NotNull
    @Override
    public Completable writePoint(@NotNull Point_FB value) {
        return null;
    }


    @NotNull
    @Override
    public Maybe<List<Point_FB>> retrievePoints(@NotNull String country, @NotNull String city, @NotNull String googleType, double latitudeFrom, double latitudeTo, double longitudeFrom, double longitudeTo, String deleted) {
        Query query = db.collection("Countries").document(country).collection("Cities").document(city)
                .collection("Types").document(googleType.equals("") ? "no_type" : googleType)
                .collection("Points").whereGreaterThanOrEqualTo("latitude", latitudeFrom-0.01).whereLessThanOrEqualTo("latitude", latitudeTo+0.01);
        return getCollection(query, latitudeFrom, latitudeTo, longitudeFrom, longitudeTo, deleted);

    }

    @NotNull
    @Override
    public Maybe<List<Point_FB>> retrieveNewPoints(@NotNull String country, @NotNull String city, @NotNull String googleType, long changeTime) {
        Query query = db.collection("Countries").document(country).collection("Cities").document(city)
                .collection("Types").document(googleType.equals("") ? "no_type" : googleType)
                .collection("Points").whereGreaterThan("changeTime", changeTime);

        return getCollection(query, 0, 0, 0, 0, "");
    }

    @NotNull
    @Override
    public long[] writePoints(@NotNull List<Point_FB> point_fbs) {
        return null;
    }
}
