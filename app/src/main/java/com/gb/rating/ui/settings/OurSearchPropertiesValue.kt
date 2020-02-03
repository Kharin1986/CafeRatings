package com.gb.rating.ui.settings

import androidx.preference.PreferenceManager
import com.gb.rating.R
import com.gb.rating.models.utils.MainApplication

const val INITIATION_ACTION = "initiation"
const val BASE_UPDATED_ACTION = "baseUpdated"
const val RELOAD_DATABASE_ACTION = "reloadBase"
const val RESTAURANT_TYPE = "ресторан"
const val FASTFOOD_TYPE = "фастфуд"
const val BAR_TYPE = "бар"
const val CAFE_TYPE = "кафе"

const val RATING_FIELD = "rating"
const val FAV_FIELD = "fav"
const val LOCATION_FIELD = "location"
const val LATITUDE_FIELD = "latitude"
const val LONGITUDE_FIELD = "longitude"
const val KM_PER_DEGREE = 111.134861111


class OurSearchPropertiesValue(
    var country: String = "",
    var city: String = "",
    var type: String = "",
    var distance: Double = -1.0, //отключено, как и отбор по location
    var sizeOfList: Long = -1,
    var action: String = "",
    var otherFilters: MutableList<MyFilter> = ArrayList(),
    var centerPoint: MyPoint? = MyPoint()
) {

    fun updateAction(action: String): OurSearchPropertiesValue {
        this.action = action
        return this
    }

    fun updateType(type: String): OurSearchPropertiesValue {
        this.type = type
        return this
    }

    fun updateCountry(country: String): OurSearchPropertiesValue {
        this.country = country
        return this
    }

    fun updateCity(city: String): OurSearchPropertiesValue {
        this.city= city
        return this
    }

    class MyFilter(
        val field: String = "",
        val where: String = "",
        val value_1: Any? = null,
        val value_2: Any? = null
    )

    class MyPoint(
        var latitude: Double = 55.753664,
        var longityde: Double = 37.619891
    )

    fun addFilter_RatingMoreOrEquel(rating: Float): OurSearchPropertiesValue {
        otherFilters.filter { it.field != RATING_FIELD }
        otherFilters.add(MyFilter(RATING_FIELD, "$RATING_FIELD >= $rating"))
        return this
    }

    fun addFilter_Favorites(fav: Boolean = true): OurSearchPropertiesValue {
        otherFilters.filter { it.field != FAV_FIELD }
        otherFilters.add(MyFilter(FAV_FIELD, "IFNULL($FAV_FIELD,0) = ${if (fav) 1 else 0}"))
        return this
    }

    fun checkLocationFilter() {
        otherFilters.filter { it.field != LOCATION_FIELD }
        if (centerPoint != null && distance > 0 && centerPoint!!.latitude != 0.0 && centerPoint!!.longityde != 0.0) {
            val distanceInDegrees: Double = distance / KM_PER_DEGREE
            otherFilters.add(
                MyFilter(
                    LOCATION_FIELD,
                    "$LATITUDE_FIELD BETWEEN ${(centerPoint!!.latitude - distanceInDegrees)} AND ${(centerPoint!!.latitude + distanceInDegrees)}" +
                            " AND ${LONGITUDE_FIELD} BETWEEN ${(centerPoint!!.longityde - distanceInDegrees)} AND ${(centerPoint!!.longityde + distanceInDegrees)}"
                )
            )
        }
    }
}

fun initialSearchProperties(): OurSearchPropertiesValue {

    val context = MainApplication.applicationContext();
    // Get the preferences
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    // Get the user dark theme settings
    val settingsCity: String = prefs.getString(context.resources.getString(R.string.CITY_FIELD),"") as String
    val settingsCountry = prefs.getString(context.resources.getString(R.string.COUNTRY_FIELD),"") as String

    return OurSearchPropertiesValue(settingsCountry, settingsCity)
}

