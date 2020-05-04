package com.gb.rating.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class UnverifiedRating {

    //ID's
    public String cafeKey = ""; // ID, ref Key to Cafe_FB
    public String fiscalId = ""; //ID to Unverified, fiscalDriveNumber (ФН) + "_" + fiscalDocumentNumber (ФД)
    public String uid = ""; //if authorized, as well as unonymously
    public long timeRateDone = new Date().getTime();

    //SERVICE ATTRIBUTES
    public boolean verified = false;
    public boolean cafeFound = false;
    public boolean processed = false; // backend must process thees raw data of Ratings,
    public String resultOfProcessing = ""; //the result of receipt processing
    public List<String> errorsOfProcessing;
    public List<String> warningsOfProcessing;


    //FISCAL ATTRIBUTES
    public String fiscalDate; // Date&Time in QR code - например, 20200210T1639
    public String retailPlaceAddress = ""; //from QR code
    public Map<String, String> fiscalAttrOthersMap = new HashMap<>();  // Consists of: String fiscalDriveNumber (ФН), String fiscalDriveNumber (ФД), String fiscalSign (ФПД), String userInn (INN of the company in QR code), String  user (Name of the company in QR code), kktRegId (Number of the KKM when IFNS it registered)

    //MAIN ATTRIBUTES
    public float rating = 0;
    public RatingsBase ratingsBaseMap = new RatingsBase();
    public String comment = "";
    public String chosenCafeType = "";

    @Exclude
    public void calculateRating() {
        rating = (ratingsBaseMap.rateKitchen + ratingsBaseMap.rateService + ratingsBaseMap.rateComfort) / 3;
    }

    public void calculateFicalID() {
        fiscalId = "" +fiscalAttrOthersMap.get("fn") + "_" + fiscalAttrOthersMap.get("i");
    }
}