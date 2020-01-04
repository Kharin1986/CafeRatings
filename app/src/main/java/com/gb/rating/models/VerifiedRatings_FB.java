package com.gb.rating.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class VerifiedRatings_FB {
    public String cafeKey = ""; // ID, ref Key to Cafe_FB
    public String fiscalId = ""; // fiscalDriveNumber (ФН) + "_" + fiscalDocumentNumber (ФД)

    public Date fiscalDate; // Date&Time in QR code
    public RatingsBase_FB ratingsBaseMap = new RatingsBase_FB();
    public String comment = "";

    public UnverifiedRatings_FB unverifiedRatings_FB = new UnverifiedRatings_FB();
}
