package com.gb.rating.filestore;

import com.gb.rating.models.OurSearchPropertiesValue;

public class Mapper {

    public static Point_FB convert(OurSearchPropertiesValue.MyPoint myPoint, double radius, OurSearchPropertiesValue ourSearchPropertiesValue,String type)
    {
        return new Point_FB(ourSearchPropertiesValue.getCountry(), ourSearchPropertiesValue.getCity(), ""+type, myPoint.getLatitude(), myPoint.getLongityde(), radius );
    }
}
