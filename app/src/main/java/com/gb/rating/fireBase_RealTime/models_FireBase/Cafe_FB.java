package com.gb.rating.fireBase_RealTime.models_FireBase;

import com.gb.rating.models.CafeItem;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Cafe_FB implements ConvertableEntity{

    //MAIN ATTRIBUTES
public String name = "";
    public String type = "";
    public String descr = "";
    public double rating = -3; //отрицательное значение, для сортировки
    public String country  = "Russian Federations";
    public String city  = "";
    public String addressMain;
    public float latitude = 0;
    public float longitude = 0;

    @Exclude
    public String cafeId = ""; //imgDir будет равно ref.key в отдельном каталоге (в CloudStorage/cafeImages)- сначала создаем кафе, затем - размещаем фото

    //ADDITIONAL ATTRIBUTES (later, others SQL tables)
    public Map<String,String> addressAttrMap = new HashMap<>();
    public List<String> addressOthersList;
    public List<String> innList;
    public RatingsBase_FB ratingsBaseMap = new RatingsBase_FB();
    public Map<String,String> propertiesMap = new HashMap<>();


    //CONSTRUCTOR FOR getValue()
    public Cafe_FB() {
    }

    public Cafe_FB(String name, String type, String descr, double rating, String country, String city, String addressMain) {
        this.name = name;
        this.type = type;
        this.descr = descr;
        this.rating = rating;
        this.country = country;
        this.city = city;
        this.addressMain = addressMain;
    }

    //METHODS For interface ConvertableEntity

    @Exclude
    @Override
    public CafeItem convertToModelEntity(){
        return  new CafeItem(0, name, type, descr, (int) -rating, country, city, "", "", "", "", 0, cafeId );
    }

    @Exclude
    public static Cafe_FB convertFromModelEntity(CafeItem cafe) {

        Cafe_FB curCafe = new Cafe_FB();

        if (cafe == null) {return curCafe;}

        curCafe.name = cafe.getName();
        curCafe.type = cafe.getType();
        curCafe.descr = cafe.getDesc();
        curCafe.rating = -cafe.getRating();
        curCafe.country = cafe.getCountry();
        curCafe.city = cafe.getCity();
        curCafe.addressMain = cafe.getStreet()+" "+cafe.getHome();
        //cafeId не нужно

        return curCafe;
    }

    @Exclude
    @Override
    public String toString() {
        return "Cafe_FB{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", descr='" + descr + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", addressMain='" + addressMain + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", cafeId='" + cafeId + '\'' +
                '}';
    }
}
