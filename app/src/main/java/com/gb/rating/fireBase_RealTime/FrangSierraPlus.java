package com.gb.rating.fireBase_RealTime;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Callable;

import durdinapps.rxfirebase2.exceptions.RxFirebaseDataException;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FrangSierraPlus {

    /**
     * Transform task to Completable {@link DatabaseReference}.
     *
     * @param intTask {@link Task<Void>}  Task or chain of Tasks
     * @return a {@link Completable} which is complete when the set value call finish successfully.
     */
    @NonNull
    public static Completable compleatableFromTask(Task<Void> intTask) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull final CompletableEmitter e) throws Exception {
                intTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        e.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (!e.isDisposed())
                            e.onError(exception);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()); //в UI можно переопределить subscribeOn, observeOn;
    }

    /**
     * Transform task to Completable {@link DatabaseReference}.
     *
     * @param callable {@link Callable < Task <Void>>}  Callable that return a Task or chain of Tasks
     * @return a {@link Completable} which is complete when the set value call finish successfully.
     */
    @NonNull
    public static Completable compleatableFromCallable(Callable<Task<Void>> callable) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull final CompletableEmitter e) throws Exception {
                callable.call().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        e.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (!e.isDisposed())
                            e.onError(exception);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()); //в UI можно переопределить subscribeOn, observeOn;
    }

    /**
     * Listener for a single change in te data at the given query location.
     *
     * @param query reference represents a particular location in your Database and can be used for reading or writing data to that Database location.
     * @return a {@link Maybe} which emits the actual state of the database for the given query. onSuccess will be only call when
     * the given {@link DataSnapshot} exists onComplete will only called when the data doesn't exist.
     */
    @NonNull
    public static Maybe<DataSnapshot> observeSingleValueEvent(@NonNull final Query query) {
        return Maybe.create(new MaybeOnSubscribe<DataSnapshot>() {
            @Override
            public void subscribe(final MaybeEmitter<DataSnapshot> emitter) throws Exception {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            emitter.onSuccess(dataSnapshot);
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
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()); //в UI можно переопределить subscribeOn, observeOn;
    }

    /**
     * Set the given value on the specified {@link DatabaseReference}.
     *
     * @param ref   reference represents a particular location in your database.
     * @param value value to update.
     * @return a {@link Completable} which is complete when the set value call finish successfully.
     */
    @NonNull
    public static Completable setValue(@NonNull final DatabaseReference ref,
                                       final Object value) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull final CompletableEmitter e) throws Exception {
                ref.setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        e.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (!e.isDisposed())
                            e.onError(exception);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()); //в UI можно переопределить subscribeOn, observeOn;
    }

}
