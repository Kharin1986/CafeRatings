package com.gb.rating.ui.search

import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import java.io.Serializable
import java.util.*

data class MapWindow(
    var mapBoundingBox: BoundingBox = BoundingBox(),
    var centerPoint: GeoPoint = GeoPoint(0.0, 0.0),
    var cafesBoundingBox: BoundingBox = BoundingBox(),
    var timeChanged: Date
) : Serializable