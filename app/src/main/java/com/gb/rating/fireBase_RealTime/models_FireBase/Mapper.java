package com.gb.rating.fireBase_RealTime.models_FireBase;

import com.gb.rating.googleMapsAPI.Nearby.Photo;
import com.gb.rating.googleMapsAPI.Nearby.Result;
import com.gb.rating.models.CafeItem;
import com.gb.rating.models.OurSearchPropertiesValue;
import com.gb.rating.models.SearchUtils;
import com.gb.rating.models.UnverifiedRating;
import com.gb.rating.models.VerifiedRating;

import java.util.List;

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
        return  new CafeItem(0, cafe.name, cafe.type, cafe.descr, (int) -cafe.rating, cafe.country, cafe.city, cafe.addressMain, "", "", "", 0, cafe.cafeId, cafe.latitude, cafe.longitude, cafe.deleted, false );

    }

    public static UnverifiedRating convert (UnverifiedRating rating){
        return (rating == null)? new UnverifiedRating() : rating;
    }

    public static VerifiedRating convert (VerifiedRating rating){
        return (rating == null)? new VerifiedRating() : rating;
    }

    public static Cafe_FB convert(Result cafeG, OurSearchPropertiesValue ourSearchPropertiesValue) {
        Cafe_FB curCafe = new Cafe_FB();
        if (cafeG == null) {return curCafe;}

        curCafe.cafeId = cafeG.getPlaceId(); //присваиваем гугловский ID
        curCafe.name = cafeG.getName();
        curCafe.type = chooseType(cafeG.getTypes());
        curCafe.rating = -cafeG.getRating(); //пусть будет гугловский коли свой пустой
        curCafe.country = ourSearchPropertiesValue.getCountry();
        curCafe.city = ourSearchPropertiesValue.getCity();
        curCafe.addressMain = cafeG.getVicinity();
        curCafe.latitude =  cafeG.getGeometry().getLocation().getLat();
        curCafe.longitude = cafeG.getGeometry().getLocation().getLng();

        curCafe.googlePlaceId = cafeG.getPlaceId();
        curCafe.googleRating = cafeG.getRating();
        curCafe.googlePriceLevel = cafeG.getPriceLevel();
        if (cafeG.getPhotos() != null && cafeG.getPhotos().size()>0) {
            curCafe.googlePhotoReference = ((Photo) cafeG.getPhotos().toArray()[0]).getPhotoReference();
        }
        return curCafe;
    }

    private static String chooseType(List<String> types) {
        if (types.contains(SearchUtils.RESTAURANT_GOOGLE)) {return SearchUtils.RESTAURANT_TYPE;}
        else if (types.contains(SearchUtils.BAR_GOOGLE)) {return SearchUtils.BAR_TYPE;}
        else if (types.contains(SearchUtils.CAFE_GOOGLE)) {return SearchUtils.CAFE_TYPE;}
        else return "";
    }


}
