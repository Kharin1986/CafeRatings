package com.gb.rating.ui.search

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.gb.rating.R
import kotlinx.android.synthetic.main.fragment_search.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.util.StorageUtils.getStorage
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*


class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private var latCenterPoint: Double = 0.0
    private  var lonCenterPoint: Double = 0.0
    private var centerPoint: GeoPoint? = null
    var UIhandler: Handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel =
            ViewModelProviders.of(this).get(SearchViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_search, container, false)
//        val ctx = activity!!.applicationContext

        getLastPosition(savedInstanceState)
        configurationMap(view)
        checkAndRequestPermissions()

        return view
    }

    private fun getLastPosition(savedInstanceState: Bundle?) {
        centerPoint = null

        if (savedInstanceState != null) {
            latCenterPoint = savedInstanceState.getDouble("latCenterPoint")
            lonCenterPoint = savedInstanceState.getDouble("lonCenterPoint")
            centerPoint = GeoPoint(latCenterPoint, lonCenterPoint)
        }
    }

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

    private fun configurationMap(v: View) {
//        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

        val provider = Configuration.getInstance()
        provider.userAgentValue = BuildConfig.APPLICATION_ID
        provider.osmdroidBasePath = getStorage()
        provider.osmdroidTileCache = getStorage()

        val map: MapView = v.findViewById(R.id.map)
        map.setUseDataConnection(true)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val mapController: IMapController
        mapController = map.getController()!!
        mapController.zoomTo(14, 1)

        val mGpsMyLocationProvider = GpsMyLocationProvider(activity)
        val mLocationOverlay = MyLocationNewOverlay(mGpsMyLocationProvider, map)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation()

        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_menu_compass)
        mLocationOverlay.setPersonIcon(icon)
        map.getOverlays().add(mLocationOverlay)

        mLocationOverlay.runOnFirstFix {
            UIhandler.post {
                if (centerPoint == null) centerPoint = mLocationOverlay.getMyLocation()
                map.getController().animateTo(centerPoint, 15.0, 100L)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (centerPoint != null) {
            outState.putDouble("latCenterPoint", centerPoint!!.getLatitude())
            outState.putDouble("lonCenterPoint", centerPoint!!.getLongitude())
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

    companion object {
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }
}


