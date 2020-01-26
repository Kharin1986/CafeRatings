package com.gb.rating.dataBase;

public class CafeDbScheme {

    public static final class CafeTable{
        public static final String NAME = "cafetable";

        public static final class Cols {
            public static final String CAFE_NAME = "city_name";
            public static final String TYPE = "type";
            public static final String DESCRIPTION = "description";
            public static final String RATING = "rating";
            public static final String COUNTRY = "country";
            public static final String CITY = "city";
            public static final String STREET = "street";
            public static final String HOME = "home";
            public static final String LOCATION = "location";
            public static final String WORK_TIME = "worktime";
            public static final String CAFE_ID = "cafeId";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String DELETED = "deleted";
        }
    }

    public static final class FavCafeTable{
        public static final String NAME = "favcafetable";

        public static final class Cols {
            public static final String CAFE_ID = "cafeId";
            public static final String FAV = "fav";
        }
    }


}