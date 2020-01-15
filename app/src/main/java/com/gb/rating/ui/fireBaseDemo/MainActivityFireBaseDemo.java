package com.gb.rating.ui.fireBaseDemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions;
import com.gb.rating.R;
import com.gb.rating.ui.fireBaseDemo.ui.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivityFireBaseDemo extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String TAG = "MainActivityFireBaseDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        TestGlid();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null){
            CommonAuthFunctions.signInAnonymously();
        }

        Log.d(TAG,"CURRENT USER CURRENT USER CURRENT USER CURRENT USER CURRENT USER CURRENT USER CURRENT USER :"+mAuth.getCurrentUser());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        mAuth = FirebaseAuth.getInstance();

        TestGreetings(myRef);
        testToken();
        testDownloadingFileToFilysystem();

    }



    private void TestGlid() {
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference ref = storageReference.child("cafeImages/cloud.jpg");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // ImageView in your Activity

                // Download directly from StorageReference using Glide
                // (See MyAppGlideModule for Loader registration)

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        ImageView imageView = findViewById(R.id.imageViewFirebase);
        Glide.with(imageView /* context */)
                .load(ref)
                .into(imageView);

    }

    private void testDownloadingFileToFilysystem() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child("cafeImages/cloud.jpg");

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }




    private void testToken() {
        try {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
        } catch (Throwable t) {};
    }


    //TEST FUNCTIONS
    private void TestGreetings(DatabaseReference myRef) {
        myRef.setValue("Hello, CafeRatings!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
