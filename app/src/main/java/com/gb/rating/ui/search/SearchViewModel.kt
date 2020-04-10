package com.gb.rating.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.rating.models.KM_PER_DEGREE
import com.gb.rating.models.OurSearchPropertiesValue
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import java.util.*
import kotlin.math.abs

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
        lastMapWindow?.let { mW ->
            if (abs(newBoundingBox.actualNorth - mW.mapBoundingBox.actualNorth) > 0.1 * abs(mW.mapBoundingBox.actualNorth - mW.mapBoundingBox.actualSouth)
                || (abs(newBoundingBox.actualSouth - mW.mapBoundingBox.actualSouth) > 0.1 * abs(mW.mapBoundingBox.actualNorth - mW.mapBoundingBox.actualSouth))
                || (abs(newBoundingBox.lonEast - mW.mapBoundingBox.lonEast) > 0.1 * abs(mW.mapBoundingBox.lonEast - mW.mapBoundingBox.lonWest))
                || (abs(newBoundingBox.lonWest - mW.mapBoundingBox.lonWest) > 0.1 * abs(mW.mapBoundingBox.lonEast - mW.mapBoundingBox.lonWest))

            ) newMapWindow = MapWindow(
                newBoundingBox,
                newBoundingBox.centerWithDateLine,
                newBoundingBox,
                saveTimeChanged)

        }

    }

    //temptable
    fun convertBoundingBoxToNewOurSearchProperties(
        newBoundingBox: BoundingBox,
        ourSearchPropertiesValue: OurSearchPropertiesValue
    ): OurSearchPropertiesValue {

        val newDistance = Math.max(
            abs(newBoundingBox.actualNorth - newBoundingBox.actualSouth)/2 * KM_PER_DEGREE,
            abs(newBoundingBox.lonEast - newBoundingBox.lonWest)/2 * KM_PER_DEGREE
        )

        val centerPointGeo = newBoundingBox.getCenterWithDateLine()
        return ourSearchPropertiesValue.copy(distance = newDistance, centerPoint = OurSearchPropertiesValue.MyPoint(centerPointGeo.latitude, centerPointGeo.longitude))
    }


    fun setLastMapWindow(it: OurSearchPropertiesValue?) {
        it?.let { spv ->
            if (spv.distance > 0 && spv.centerPoint.latitude != 0.0 && spv.centerPoint.longityde != 0.0) {
                if (lastMapWindow == null) {
                    val centerPoint =
                        GeoPoint(spv.centerPoint.latitude, spv.centerPoint.longityde)
                    val distanceInDegrees: Double = spv.distance / KM_PER_DEGREE
                    val boundingBox = BoundingBox(
                        centerPoint.latitude + distanceInDegrees,
                        centerPoint.longitude + distanceInDegrees,
                        centerPoint.latitude - distanceInDegrees,
                        centerPoint.longitude - distanceInDegrees
                    )
                    lastMapWindow = MapWindow(
                        boundingBox,
                        centerPoint,
                        boundingBox,
                        Calendar.getInstance().time
                    )
                }
            }
        }
    }

}