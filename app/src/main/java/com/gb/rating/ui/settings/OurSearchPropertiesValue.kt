package com.gb.rating.ui.settings

const val INITIATION_ACTION = "initiation"
const val BASE_UPDATED_ACTION = "baseUpdated"
const val RESTAURANT_TYPE = "ресторан"
const val FASTFOOD_TYPE = "фастфуд"
const val BAR_TYPE = "бар"
const val CAFE_TYPE = "кафе"

const val RATING_FIELD = "rating"
const val FAV_FIELD = "fav"


class OurSearchPropertiesValue(
    var country : String = "",
    var city : String = "",
    var type : String = "",
    var distance : Double = 5.0,
    var sizeOfList : Long = -1,
    var action : String = "",
    var otherFilters: MutableList<MyFilter> = ArrayList()

){
    fun updateAction(action: String ) : OurSearchPropertiesValue{
        this.action = action
        return this
    }

    fun updateType(type: String ) : OurSearchPropertiesValue{
        this.type = type
        return this
    }


    class MyFilter(
        val field: String = "",
        val where: String = "",
        val value_1: Any? = null,
        val value_2: Any? = null
    )

    fun addFilter_RatingMoreOrEquel(rating: Float) : OurSearchPropertiesValue{
        otherFilters.filter {it.field != RATING_FIELD}
        otherFilters.add(MyFilter(RATING_FIELD,"$RATING_FIELD >= $rating"))
        return this
    }

    fun addFilter_Favorites(fav : Boolean = true) : OurSearchPropertiesValue{
        otherFilters.filter {it.field != FAV_FIELD}
        otherFilters.add(MyFilter(FAV_FIELD,"IFNULL($FAV_FIELD,0) = ${if (fav) 1 else 0}"))
        return this
    }

}

fun initialSearchProperties(): OurSearchPropertiesValue {
    return OurSearchPropertiesValue("Россия", "Москва")
    }

