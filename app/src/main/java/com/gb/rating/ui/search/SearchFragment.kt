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
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.gb.rating.R
import com.gb.rating.models.*
import com.gb.rating.models.utils.MainApplication
import com.gb.rating.ui.ViewModelMain
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapAdapter
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
    private var lonCenterPoint: Double = 0.0
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

    fun getLastPosition(savedInstanceState: Bundle?) {
        searchViewModel.lastMapWindow = null
        if (savedInstanceState != null) {
            searchViewModel.lastMapWindow =
                savedInstanceState.getSerializable("lastMapWindow") as? MapWindow
        }
    }

    override fun onStart() {
        super.onStart()

        prepareMapAfterOnStart()
        setCafeListObserver() //подписка на обновление листа
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchViewModel.lastMapWindow?.let {
            outState.putSerializable("lastMapWindow", it.centerPoint)
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

    override fun onDestroy() {
        super.onDestroy()

        searchViewModel.lastMapWindow?.let {
            val prefs = PreferenceManager.getDefaultSharedPreferences(MainApplication.applicationContext())
            prefs.edit (false,{putFloat("INITIAL_LATITUDE", it.centerPoint.latitude.toFloat()); putFloat("INITIAL_LONGITUDE", it.centerPoint.longitude.toFloat())})
        }
    }

    //-------------------------- TECHNICAL FUNCTIONS ----------------------------------------------------
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //-------------------------- MAP CONFIGURATION ----------------------------------------------------

    private fun checkAndRequestPermissions() {
        val permissionWrite = ContextCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionLocation = ContextCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        val listPermissionsNeeded = ArrayList<String>()

        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity!!,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun configurationMap(v: View) {
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))

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
                searchViewModel.lastMapWindow?.let {
                    map.controller.animateTo(it.centerPoint)
                }
            }
        }

        map.post(object : Runnable { // map.height выдавало 0
            override fun run() {
                observeOurSearchProperties() //подписка на обновление поисковых условий
            }
        })

        map.addMapListener(object : MapAdapter() {
            override fun onScroll(event: ScrollEvent?): Boolean {
                event?.let { checkWhatToDOWithNewEvent(event.source.projection.boundingBox) }
                return super.onScroll(event)
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                event?.let { checkWhatToDOWithNewEvent(event.source.projection.boundingBox) }
                return super.onZoom(event)
            }
        })
    }

    fun checkWhatToDOWithNewEvent(newBoundingBox: BoundingBox) {
        val saveTimeChanged = Calendar.getInstance().time
        searchViewModel.setNewMapWindow(newBoundingBox, saveTimeChanged)

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            val newTime = searchViewModel.newMapWindow?.timeChanged?.time ?: 0
            if (newTime == saveTimeChanged.time) {
                searchViewModel.lastMapWindow = searchViewModel.newMapWindow!!.copy(timeChanged = Calendar.getInstance().time)
                searchViewModel.newMapWindow = null

                activityViewModel.ourSearchProperties_update(
                    activityViewModel.ourSearchPropertiesValue().updateBoundingBox(newBoundingBox)
                )
            }
        }

    }


    //-------------------------- MAP CONFIGURATION ----------------------------------------------------
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //-------------------------- MAIN FUNCTIONS ----------------------------------------------------

    private fun setCafeListObserver() {
        activityViewModel.cafelist().observe(this, Observer {
            it?.let { cafeItem ->
                refreshOverlay(cafeItem)
            }
        })
    }

    private fun observeOurSearchProperties() {
        activityViewModel.ourSearchProperties().observe(this, Observer {
            if (searchViewModel.lastMapWindow == null) {
                searchViewModel.setLastMapWindow(it)
                searchViewModel.lastMapWindow?.let { lMW ->
                    map.zoomToBoundingBox(
                        lMW.mapBoundingBox,
                        true
                    )
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

        if (map.overlays.size>1) map.overlays.removeAt(map.overlays.size-1)
        return map.overlays.add(mOverlay)
    }
    //-------------------------- MAIN FUNCTIONS ----------------------------------------------------
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
