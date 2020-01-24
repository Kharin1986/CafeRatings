package com.gb.rating.fireBase_RealTime.models_FireBase;

import com.gb.rating.models.CafeItem;
import com.gb.rating.models.UnverifiedRating;
import com.gb.rating.models.VerifiedRating;

public class Mapper {

    public static Cafe_FB convert(CafeItem cafe){

        Cafe_FB curCafe = new Cafe_FB();
        if (cafe == null) {return curCafe;}

        curCafe.name = cafe.getName();
        curCafe.type = cafe.getType();
        curCafe.descr = cafe.getDesc();
        curCafe.rating = -cafe.getRating();
        curCafe.country = cafe.getCountry();
        curCafe.city = cafe.getCity();
        curCafe.addressMain = cafe.getStreet()+" "+cafe.getHome();
        curCafe.cafeId = cafe.getCafeId();
        curCafe.latitude = cafe.getLatitude();
        curCafe.longitude = cafe.getLongitude();
        curCafe.deleted = cafe.getDeleted();
        //cafeId, fav  не нужнs


        return curCafe;
    }

    public static CafeItem convert(Cafe_FB cafe){
        return  new CafeItem(0, cafe.name, cafe.type, cafe.descr, (int) -cafe.rating, cafe.country, cafe.city, "", "", "", "", 0, cafe.cafeId, cafe.latitude, cafe.longitude, cafe.deleted, false );

    }

    public static UnverifiedRating convert (UnverifiedRating rating){
        return (rating == null)? new UnverifiedRating() : rating;
    }

    public static VerifiedRating convert (VerifiedRating rating){
        return (rating == null)? new VerifiedRating() : rating;
    }

}
