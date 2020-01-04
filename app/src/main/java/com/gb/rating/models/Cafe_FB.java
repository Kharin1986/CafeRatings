package com.gb.rating.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Cafe_FB {
    // public int imgDir = 0;  imgDir будет равно ref.key в отдельном каталоге (или FireBase Cloudbase)- сначала создаем кафе, затем - размещаем фото
    public String name = "";
    public String type = "";
    public String descr = "";
    public String addressMain;
    public List<String> addressOthersList;
    public Map<String,String> addressAttrMap = new HashMap<>();
    public List<String> innList;

    public UnverifiedRatings_FB unverifiedRatings_FB = new UnverifiedRatings_FB();

    public float latitude = 0;
    public float longitude = 0;
    public Map<String,String> propertiesMap = new HashMap<>();

    public Cafe_FB() {
    }
}
