package com.gb.rating.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

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
    public float rating = 3;
    public String addressMain;
    public float latitude = 0;
    public float longitude = 0;

    //ADDITIONAL ATTRIBUTES
    public Map<String,String> addressAttrMap = new HashMap<>();
    public List<String> addressOthersList;
    public List<String> innList;
    public RatingsBase_FB ratingsBaseMap = new RatingsBase_FB();
    public Map<String,String> propertiesMap = new HashMap<>();


    //CONSTRUCTOR FOR getValue()
    public Cafe_FB() {
    }
}
