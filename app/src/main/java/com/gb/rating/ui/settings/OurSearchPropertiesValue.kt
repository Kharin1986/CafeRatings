@file:JvmName("SearchUtils")

package com.gb.rating.ui.settings

import androidx.preference.PreferenceManager
import com.gb.rating.R
import com.gb.rating.models.utils.MainApplication
import kotlin.math.sqrt
import kotlin.math.pow

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
const val EARTH_RADIUS = 6371.0


class OurSearchPropertiesValue(
    var country: String = "",
    var city: String = "",
    var type: String = "",
    var distance: Double = -1.0, //отключено, как и отбор по location
    var sizeOfList: Long = -1,
    var action: String = "",
    var otherFilters: MutableList<MyFilter> = ArrayList(),
    var centerPoint: MyPoint = MyPoint()
) {

    fun updateAction(action: String): OurSearchPropertiesValue {
        this.action = action
        return this
    }

    fun updateType(type: String): OurSearchPropertiesValue {
        this.type = type
        updateAction("")
        return this
    }

    fun updateCountry(country: String): OurSearchPropertiesValue {
        this.country = country
        updateAction("")
        return this
    }

    fun updateCity(city: String): OurSearchPropertiesValue {
        this.city = city
        updateAction("")
        return this
    }

    fun updateDistance(distance: Double): OurSearchPropertiesValue {
        this.distance = distance
        updateAction("")
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
        otherFilters = otherFilters.filter { it.field != RATING_FIELD } as MutableList<MyFilter>
        otherFilters.add(MyFilter(RATING_FIELD, "$RATING_FIELD >= $rating"))
        return this
    }

    fun addFilter_Favorites(fav: Boolean = true): OurSearchPropertiesValue {
        otherFilters = otherFilters.filter { it.field != FAV_FIELD } as MutableList<MyFilter>
        otherFilters.add(MyFilter(FAV_FIELD, "IFNULL($FAV_FIELD,0) = ${if (fav) 1 else 0}"))
        return this
    }

    fun checkLocationFilter() {
        otherFilters = otherFilters.filter { it.field != LOCATION_FIELD } as MutableList<MyFilter>
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

    val context = MainApplication.applicationContext()
    // Get the preferences
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    val settingsCity: String =
        prefs.getString(context.resources.getString(R.string.CITY_KEY), "").toString()
    val settingsCountry =
        prefs.getString(context.resources.getString(R.string.COUNTRY_KEY), "").toString()

    return OurSearchPropertiesValue(settingsCountry, settingsCity, "", countDistance())
}

fun countDistance(newValue: Any? = null): Double {
    val context = MainApplication.applicationContext()
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val distanceFilter: Boolean = if (newValue is Boolean) {
        newValue
    } else {
        prefs.getBoolean(context.resources.getString(R.string.DISTANCEFILTER_KEY), true)
    }

    val settingsDistance: Int = if (!distanceFilter) {
        -1
    } else if (newValue is Int) {
        newValue
    } else {
        prefs.getInt(context.resources.getString(R.string.DISTANCE_KEY), 0)
    }
    val realDistance: Double = settingsDistance.toDouble() / 10

    return realDistance
}


fun countDistanceToAnotherPoint(
    centerPoint: OurSearchPropertiesValue.MyPoint,
    anotherPoint: OurSearchPropertiesValue.MyPoint
): Double {

    require(!(anotherPoint.latitude.equals(0) || anotherPoint.longityde.equals(0))) { "countDistanceToAnotherPoint" }
    require(!(centerPoint.latitude.equals(0) || centerPoint.longityde.equals(0))) { "countDistanceToAnotherPoint" }

    return EARTH_RADIUS*sqrt(((anotherPoint.latitude - centerPoint.latitude)*Math.PI/180).pow(2.0) + ((anotherPoint.longityde - centerPoint.longityde)*Math.PI/180).pow(2.0))
}

