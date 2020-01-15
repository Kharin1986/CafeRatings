package com.gb.rating.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Date;

@IgnoreExtraProperties
public class VerifiedRating {

    //ID's
    public String cafeKey = ""; // ID, ref Key to Cafe_FB
    public String fiscalId = ""; //ID to Verified, fiscalDriveNumber (ФН) + "_" + fiscalDocumentNumber (ФД)
    public String uid = ""; //if authorized, as well as unonymously

    //MAIN ATTRIBUTES()
    public Date fiscalDate; // Date&Time in QR code
    public float rating = 0;
    public RatingsBase ratingsBaseMap = new RatingsBase();
    public String comment = "";

    public VerifiedRating() {
    }

}
