package com.gb.rating.fireBase_RealTime.models_FireBase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Date;

@IgnoreExtraProperties
public class VerifiedRating_FB implements ConvertableEntity {

    //ID's
    public String cafeKey = ""; // ID, ref Key to Cafe_FB
    public String fiscalId = ""; //ID to Verified, fiscalDriveNumber (ФН) + "_" + fiscalDocumentNumber (ФД)
    public String uid = ""; //if authorized, as well as unonymously

    //MAIN ATTRIBUTES()
    public Date fiscalDate; // Date&Time in QR code
    public float rating = 0;
    public RatingsBase_FB ratingsBaseMap = new RatingsBase_FB();
    public String comment = "";

    public VerifiedRating_FB() {
    }

    //METHODS For interface ConvertableEntity
    @Exclude
    @Override
    public VerifiedRating_FB convertToModelEntity() {
        return this;
    }

    @Exclude
    public static VerifiedRating_FB convertFromModelEntity(VerifiedRating_FB rating) {

        if (rating == null) {return new VerifiedRating_FB();}

        return rating;
    }


}
