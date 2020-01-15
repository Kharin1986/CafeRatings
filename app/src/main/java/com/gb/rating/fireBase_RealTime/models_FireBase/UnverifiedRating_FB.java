package com.gb.rating.fireBase_RealTime.models_FireBase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class UnverifiedRating_FB implements ConvertableEntity{

    //ID's
    public String cafeKey = ""; // ID, ref Key to Cafe_FB
    public String fiscalId = ""; //ID to Unverified, fiscalDriveNumber (ФН) + "_" + fiscalDocumentNumber (ФД)
    public String uid = ""; //if authorized, as well as unonymously

    //SERVICE ATTRIBUTES
    public boolean verified = false;
    public boolean cafeFound = false;
    public boolean processed = false; // backend must process thees raw data of Ratings,
    public String resultOfProcessing = ""; //the result of receipt processing
    public List<String> errorsOfProcessing ;
    public List<String> warningsOfProcessing;


    //FISCAL ATTRIBUTES
    public Date fiscalDate; // Date&Time in QR code
    public String retailPlaceAddress = ""; //from QR code
    public Map<String,String> fiscalAttrOthersMap = new HashMap<>();
        // Consists of:
        //    String fiscalDriveNumber; // ФН
        //    String fiscalDriveNumber; // ФД
        //    String fiscalSign; // ФПД
        //    String userInn; //INN of the company in QR code
        //    String user; //Name of the company in QR code
        //    String kktRegId; //Number of the KKM when IFNS it registered

    //MAIN ATTRIBUTES
    public float rating = 0;
    public RatingsBase_FB ratingsBaseMap = new RatingsBase_FB();
    public String comment = "";


    //METHODS For interface ConvertableEntity

    public UnverifiedRating_FB() {
    }

    @Exclude
    @Override
    public UnverifiedRating_FB convertToModelEntity() {
        return this;
    }


    @Exclude
    public static UnverifiedRating_FB convertFromModelEntity(UnverifiedRating_FB rating) {

        if (rating == null) {return new UnverifiedRating_FB();}

        return rating;
    }
}