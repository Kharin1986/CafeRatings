package com.gb.rating.firebase_storage;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageStaticFunctions {


    public static StorageReference getReftoImageCatalog(){
        return FirebaseStorage.getInstance().getReference().child("cafeImages");
    }
}
