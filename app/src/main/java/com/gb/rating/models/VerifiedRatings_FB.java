package com.gb.rating.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class VerifiedRatings_FB {

    //ID's
    public String cafeKey = ""; // ID, ref Key to Cafe_FB
    public String fiscalId = ""; //ID to Unferified, fiscalDriveNumber (ФН) + "_" + fiscalDocumentNumber (ФД)

    //MAIN ATTRIBUTES()
    public Date fiscalDate; // Date&Time in QR code
    public float rating = 0;
    public RatingsBase_FB ratingsBaseMap = new RatingsBase_FB();
    public String comment = "";


    //CONSTRUCTOR FOR getValue()
    public VerifiedRatings_FB() {
    }
}
