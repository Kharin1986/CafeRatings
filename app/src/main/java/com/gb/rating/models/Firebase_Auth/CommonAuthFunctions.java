package com.gb.rating.models.Firebase_Auth;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CommonAuthFunctions {

    private static FirebaseAuth mAuth(){
        return FirebaseAuth.getInstance();
    }

    public static void signInAnonymously() {
        String TAG = "Authentication";

        // Sign in anonymously. Authentication is required to read or write from Firebase Storage.
        mAuth().signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signInAnonymously:SUCCESS");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    public static void checkAuth(){
        if (mAuth().getCurrentUser() == null){
            CommonAuthFunctions.signInAnonymously();
        }
    }


    public static String getUid(){
        FirebaseUser user = mAuth().getCurrentUser();
        return (user == null)? "" : user.getUid();
    }
}
