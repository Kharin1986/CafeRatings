package com.gb.rating.ui.search

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.rating.models.utils.UpdateDatabase
import com.gb.rating.models.KM_PER_DEGREE
import com.gb.rating.models.OurSearchPropertiesValue
import com.gb.rating.models.boundingBoxToMyPoint
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import java.util.*
import kotlin.math.abs

const val MAP_PRECISION_RATE = 0.1
const val DEFAULT_SCREEN_DISTANCE = 2.0
const val ADDITIONAL_LOAD_FROM_GOOGLE_MAP_API = true

class SearchViewModel : ViewModel() {

    var lastMapWindow: MapWindow? = null
    var newMapWindow: MapWindow? = null

    private val _text = MutableLiveData<String>().apply {
        value = "This is search Fragment"
    }
    val text: LiveData<String> = _text


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //-------------------------- MAIN FUNCTIONS ----------------------------------------------------


    fun setNewMapWindow(newBoundingBox: BoundingBox, saveTimeChanged: Date) {
        if (lastMapWindow != null) {
            if ((abs(newBoundingBox.actualNorth + newBoundingBox.actualSouth) + abs(newBoundingBox.lonEast + newBoundingBox.lonWest)) > 0.1) {
                newMapWindow = MapWindow(
                    newBoundingBox.clone(),
                    newBoundingBox.centerWithDateLine,
                    newBoundingBox.clone(),
                    saveTimeChanged
                )
            }
        }
    }

    fun checkIfBoundsMovedSignificantly(newBoundingBox: BoundingBox): Boolean {
        lastMapWindow?.let { old ->
            return (abs(newBoundingBox.actualNorth - old.mapBoundingBox.actualNorth) > MAP_PRECISION_RATE * abs(
                old.mapBoundingBox.actualNorth - old.mapBoundingBox.actualSouth
            )
                    || (abs(newBoundingBox.actualSouth - old.mapBoundingBox.actualSouth) > MAP_PRECISION_RATE * abs(
                old.mapBoundingBox.actualNorth - old.mapBoundingBox.actualSouth
            ))
                    || (abs(newBoundingBox.lonEast - old.mapBoundingBox.lonEast) > MAP_PRECISION_RATE * abs(
                old.mapBoundingBox.lonEast - old.mapBoundingBox.lonWest
            ))
                    || (abs(newBoundingBox.lonWest - old.mapBoundingBox.lonWest) > MAP_PRECISION_RATE * abs(
                old.mapBoundingBox.lonEast - old.mapBoundingBox.lonWest
            ))
                    ) && (abs(newBoundingBox.actualNorth + newBoundingBox.actualSouth) + abs(
                newBoundingBox.lonEast + newBoundingBox.lonWest
            )) > 0.1
        } ?: return false
    }

    //temptable
    fun convertBoundingBoxToNewOurSearchProperties(
        newBoundingBox: BoundingBox,
        ourSearchPropertiesValue: OurSearchPropertiesValue
    ): OurSearchPropertiesValue {

        val newDistance = Math.max(
            abs(newBoundingBox.actualNorth - newBoundingBox.actualSouth) / 2 * KM_PER_DEGREE,
            abs(newBoundingBox.lonEast - newBoundingBox.lonWest) / 2 * KM_PER_DEGREE
        )

        return ourSearchPropertiesValue.copy(
            distance = newDistance,
            centerPoint = boundingBoxToMyPoint(newBoundingBox)
        )
    }



    fun checkAndLoadFromGoogleAPI(boundingBox: BoundingBox, ourSearchPropertiesValue: OurSearchPropertiesValue){

        //TODO

        //toDO - не совсем правильно - не нужно грузить базу каждый раз
        if (!ADDITIONAL_LOAD_FROM_GOOGLE_MAP_API) return
        val callback = Handler.Callback { true }
        UpdateDatabase.getInstance().LoadGoogleCafeForPoint(ourSearchPropertiesValue, boundingBox, boundingBoxToMyPoint(boundingBox), callback)
    }


    fun InitialSetLastMapWindow(ourSearchPropertiesValue: OurSearchPropertiesValue?) {
        ourSearchPropertiesValue?.let { spv ->
            val newDistance= if (spv.distance > 0) spv.distance else DEFAULT_SCREEN_DISTANCE
            if (spv.centerPoint.latitude != 0.0 && spv.centerPoint.longityde != 0.0) {
                if (lastMapWindow == null) {
                    val centerPoint =
                        GeoPoint(spv.centerPoint.latitude, spv.centerPoint.longityde)
                    val distanceInDegrees: Double = newDistance / KM_PER_DEGREE
                    val boundingBox = BoundingBox(
                        centerPoint.latitude + distanceInDegrees,
                        centerPoint.longitude + distanceInDegrees,
                        centerPoint.latitude - distanceInDegrees,
                        centerPoint.longitude - distanceInDegrees
                    )
                    lastMapWindow = MapWindow(
                        boundingBox.clone(),
                        centerPoint,
                        boundingBox.clone(),
                        Calendar.getInstance().time
                    )
                }
            }
        }
    }

}