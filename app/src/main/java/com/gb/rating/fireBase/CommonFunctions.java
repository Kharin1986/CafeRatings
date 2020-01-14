package com.gb.rating.fireBase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

public class CommonFunctions {

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
        });
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
        });
    }























}
