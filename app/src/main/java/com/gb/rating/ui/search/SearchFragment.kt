package com.gb.rating.ui.search

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gb.rating.R
import com.gb.rating.models.*
import com.gb.rating.ui.ViewModelMain
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapAdapter
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.util.StorageUtils.getStorage
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus





class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModel()
    private val activityViewModel: ViewModelMain by sharedViewModel()
    private var latCenterPoint: Double = 0.0
    private  var lonCenterPoint: Double = 0.0
    private var centerPoint: GeoPoint? = null
    var UIhandler: Handler = Handler()

    companion object {
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //-------------------------- TECHNICAL FUNCTIONS ----------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        getLastPosition(savedInstanceState)
        configurationMap(view)
        checkAndRequestPermissions()

        return view
    }

    override fun onStart() {
        super.onStart()

        prepareMapAfterOnStart()
        setLiveDataObservers() //подписка на обновление листа
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (centerPoint != null) {
            outState.putDouble("latCenterPoint", centerPoint!!.latitude)
            outState.putDouble("lonCenterPoint", centerPoint!!.longitude)
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    private fun getLastPosition(savedInstanceState: Bundle?) {
        centerPoint = null

        if (savedInstanceState != null) {
            latCenterPoint = savedInstanceState.getDouble("latCenterPoint")
            lonCenterPoint = savedInstanceState.getDouble("lonCenterPoint")
            centerPoint = GeoPoint(latCenterPoint, lonCenterPoint)
        }
    }

    //-------------------------- TECHNICAL FUNCTIONS ----------------------------------------------------
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //-------------------------- MAP CONFIGURATION ----------------------------------------------------

    private fun checkAndRequestPermissions() {
        val permissionWrite = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionLocation = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION)

        val listPermissionsNeeded = ArrayList<String>()

        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity!!, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun configurationMap(v: View) {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

        val provider = Configuration.getInstance()
        provider.userAgentValue = BuildConfig.APPLICATION_ID
        provider.osmdroidBasePath = getStorage()
        provider.osmdroidTileCache = getStorage()

        val map: MapView = v.findViewById(R.id.map)
        map.setDestroyMode(false)
        map.setUseDataConnection(true)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
    }

    private fun prepareMapAfterOnStart() {
        val mGpsMyLocationProvider = GpsMyLocationProvider(activity)
        val mLocationOverlay = MyLocationNewOverlay(mGpsMyLocationProvider, map)
        mLocationOverlay.enableMyLocation()
        //        mLocationOverlay.enableFollowLocation() //передвижение экрана вслед за локацией

        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_menu_compass)
        mLocationOverlay.setPersonIcon(icon)
        map.overlays.add(mLocationOverlay)

        mLocationOverlay.runOnFirstFix {
            UIhandler.post {
                if (centerPoint == null) centerPoint = mLocationOverlay.myLocation
                //                map.controller.animateTo(centerPoint, 15.0, 100L)
                map.controller.animateTo(centerPoint)
            }
        }

        map.post(object : Runnable { // map.height выдавало 0
            override fun run() {
                observeOurSearchProperties() //подписка на обновление поисковых условий
            }
        })

        map.addMapListener(object : MapAdapter() {
            override fun onScroll(event: ScrollEvent?): Boolean {
                return super.onScroll(event)
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                return super.onZoom(event)
            }
        })
    }

    //-------------------------- MAP CONFIGURATION ----------------------------------------------------
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //-------------------------- MAIN FUNCTIONS ----------------------------------------------------

    private fun setLiveDataObservers() {
        activityViewModel.cafelist().observe(this, Observer {
            it?.let { cafeItem ->
                refreshOverlay(cafeItem)
            }
        })
    }


    private fun observeOurSearchProperties() {
        activityViewModel.ourSearchProperties().observe(this, Observer {
            it?.let { spv ->
                if (spv.distance > 0 && spv.centerPoint.latitude != 0.0 && spv.centerPoint.longityde != 0.0) {
                    val distanceInDegrees: Double = spv.distance / KM_PER_DEGREE
                    if (centerPoint == null) centerPoint = GeoPoint(spv.centerPoint.latitude, spv.centerPoint.longityde)

                    centerPoint?.let {sp-> map.zoomToBoundingBox(
                        BoundingBox(
                            sp.latitude + distanceInDegrees,
                            sp.longitude + distanceInDegrees,
                            sp.latitude - distanceInDegrees,
                            sp.longitude - distanceInDegrees
                        ), false
                    )}
                }
            }
        })
    }

    private fun refreshOverlay(cafeItem: List<CafeItem>): Boolean {
        val items = ArrayList<OverlayItem>()
        cafeItem.forEach {
            items.add(
                OverlayItem(
                    it.name,
                    it.desc,
                    GeoPoint(it.latitude.toDouble(), it.longitude.toDouble())
                )
            ) // Lat/Lon decimal degrees
        }

        //the overlay
        val mOverlay = ItemizedOverlayWithFocus(
            items,
            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    //do something
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    return false
                }
            }, context
        )
        mOverlay.setFocusItemsOnTap(true)

        return map.overlays.add(mOverlay)
    }

}
