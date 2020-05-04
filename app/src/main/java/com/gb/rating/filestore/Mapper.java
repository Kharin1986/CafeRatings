package com.gb.rating.filestore;
import com.gb.rating.dataRoom.Point_Room;
import com.gb.rating.models.OurSearchPropertiesValue;
import com.gb.rating.models.utils.Point;

public class Mapper {

    public static Point_FB convert(OurSearchPropertiesValue.MyPoint myPoint, double radius, OurSearchPropertiesValue ourSearchPropertiesValue,String type)
    {
        return new Point_FB(ourSearchPropertiesValue.getCountry(), ourSearchPropertiesValue.getCity(), ""+type, myPoint.getLatitude(), myPoint.getLongityde(), radius );
    }

    public  static Point_Room convert (Point_FB point_fb) {
        return new Point_Room(point_fb.country, point_fb.city, point_fb.type, point_fb.latitude, point_fb.longitude, point_fb.radius, point_fb.changeTime, point_fb.deleted);
    }
}
