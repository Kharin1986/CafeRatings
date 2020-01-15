package com.gb.rating.models;

import java.util.HashMap;
import java.util.Map;

public class RatingsBase {
    public float rateKitchen = 0;
    public float rateService = 0;
    public float rateComfort = 0;
    public Map<String,String> rateOthersMap = new HashMap<>();
}
