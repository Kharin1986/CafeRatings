package com.gb.rating;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;


public class FirebaseTest {

    @Test
    public void First(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance("http://localhost:9000")
                .getReference();
        dbRef.setValue("Hello Emulator!");

    }


}
