package com.gb.rating.fireBase.models_FireBase;

import com.gb.rating.models.CafeItem;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Cafe_FB {
    //MAIN ATTRIBUTES
    // public int imgDir = 0;  imgDir будет равно ref.key в отдельном каталоге (или FireBase Cloudbase)- сначала создаем кафе, затем - размещаем фото
    public String name = "";
    public String type = "";
    public String descr = "";
    public double rating = 3;
    public String country  = "Russian Federations";
    public String city  = "";
    public String addressMain;
    public float latitude = 0;
    public float longitude = 0;

    //ADDITIONAL ATTRIBUTES (later, others SQL tables)
    public Map<String,String> addressAttrMap = new HashMap<>();
    public List<String> addressOthersList;
    public List<String> innList;
    public RatingsBase_FB ratingsBaseMap = new RatingsBase_FB();
    public Map<String,String> propertiesMap = new HashMap<>();


    //CONSTRUCTOR FOR getValue()
    public Cafe_FB() {
    }

    @Exclude
    public static CafeItem convertModelEntity(Cafe_FB c){
        return  new CafeItem(0, c.name, c.type, c.descr, (int) c.rating, c.country, c.city, "", "", "", "", 0 );
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
}
