package com.gb.rating.ui.settings

const val INITIATION_ACTION = "initiation"
const val BASE_UPDATED_ACTION = "baseUpdated"

class OurSearchPropertiesValue(
    var country : String = "",
    var city : String = "",
    var type : String = "",
    var distance : Double = 5.0,
    var sizeOfList : Long = -1,
    var action : String = ""
){
    fun updateAction(action: String ) : OurSearchPropertiesValue{
        this.action = action
        return this
    }
}

fun initialSearchProperties(): OurSearchPropertiesValue {
    return OurSearchPropertiesValue("Россия", "Москва").updateAction(INITIATION_ACTION)
    }


