package com.gb.rating.fireBase.dataStore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gb.rating.fireBase.models_FireBase.Cafe_FB;
import com.gb.rating.fireBase.models_FireBase.UnverifiedRatings_FB;
import com.gb.rating.fireBase.models_FireBase.VerifiedRatings_FB;
import com.gb.rating.models.CafeItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import durdinapps.rxfirebase2.exceptions.RxFirebaseDataException;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class FrangSierraPlus {

    /**
     * Listener for a single change in te data at the given query location.
     *
     * @param query reference represents a particular location in your Database and can be used for reading or writing data to that Database location.
     * @return a {@link Maybe} which emits the actual state of the database for the given query. onSuccess will be only call when
     * the given {@link DataSnapshot} exists onComplete will only called when the data doesn't exist.
     */
    @NonNull
    public static Maybe<List<CafeItem>> observeSingleValueEvent_CafeItemList(@NonNull final Query query) {
        return Maybe.create(new MaybeOnSubscribe<List<CafeItem>>() {
            @Override
            public void subscribe(final MaybeEmitter<List<CafeItem>> emitter) throws Exception {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            List<CafeItem> cafeList= new ArrayList();

                            for (DataSnapshot CafeSnapshot: dataSnapshot.getChildren()) {
                                Cafe_FB curCafe=CafeSnapshot.getValue(Cafe_FB.class);
                                cafeList.add(curCafe.convertToModelEntity());
                            }

                            emitter.onSuccess(cafeList);
                        } else {
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!emitter.isDisposed())
                            emitter.onError(new RxFirebaseDataException(error));
                    }
                });
            }
        });
    }

    @NonNull
    public static Maybe<List<UnverifiedRatings_FB>> observeSingleValueEvent_UnverifiedRatingsList(@NonNull final Query query) {
        return Maybe.create(new MaybeOnSubscribe<List<UnverifiedRatings_FB>>() {
            @Override
            public void subscribe(final MaybeEmitter<List<UnverifiedRatings_FB>> emitter) throws Exception {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            List<UnverifiedRatings_FB> ratingsList= new ArrayList();

                            for (DataSnapshot curSnapshot: dataSnapshot.getChildren()) {
                                UnverifiedRatings_FB curRating=curSnapshot.getValue(UnverifiedRatings_FB.class);
                                ratingsList.add(curRating.convertToModelEntity());
                            }

                            emitter.onSuccess(ratingsList);
                        } else {
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!emitter.isDisposed())
                            emitter.onError(new RxFirebaseDataException(error));
                    }
                });
            }
        });
    }


    @NonNull
    public static Maybe<List<VerifiedRatings_FB>> observeSingleValueEvent_VerifiedRatingsList(@NonNull final Query query) {
        return Maybe.create(new MaybeOnSubscribe<List<VerifiedRatings_FB>>() {
            @Override
            public void subscribe(final MaybeEmitter<List<VerifiedRatings_FB>> emitter) throws Exception {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            List<VerifiedRatings_FB> ratingsList= new ArrayList();

                            for (DataSnapshot curSnapshot: dataSnapshot.getChildren()) {
                                VerifiedRatings_FB curRating=curSnapshot.getValue(VerifiedRatings_FB.class);
                                ratingsList.add(curRating.convertToModelEntity());
                            }

                            emitter.onSuccess(ratingsList);
                        } else {
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!emitter.isDisposed())
                            emitter.onError(new RxFirebaseDataException(error));
                    }
                });
            }
        });
    }


}
