package com.gb.rating

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.gb.rating.models.*
import com.gb.rating.ui.ViewModelMain
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    val viewModelMain: ViewModelMain by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.setDefaultValues(this, R.xml.main_settings, false)
        initViewModel()
        initNavControllerAndActionBar()
    }

    private fun initViewModel() {
        //viewModelMain = ViewModelProvider(this).get(ViewModelMain::class.java)
        viewModelMain.toString()
    }

    private fun initNavControllerAndActionBar() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_review,
                R.id.navigation_list,
                R.id.navigation_search,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView.setupWithNavController(navController!!)
    }

    // Кнопки домашней страницы)
    fun onRestClick(view: View) {
        viewModelMain.ourSearchProperties_update(
            initialSearchProperties().updateType(RESTAURANT_TYPE)
        )
        navController!!.navigate(R.id.navigation_list)
    }

    fun onBarClick(view: View) {
        viewModelMain.ourSearchProperties_update(
            initialSearchProperties().updateType(BAR_TYPE)
        )
        navController!!.navigate(R.id.navigation_list)
    }

    fun onTopClick(view: View) {
        viewModelMain.ourSearchProperties_update(
            initialSearchProperties().addFilter_RatingMoreOrEquel(4.5f)
        )
        navController!!.navigate(R.id.navigation_list)
    }

    fun onFavClick(view: View) {
        viewModelMain.ourSearchProperties_update(
            initialSearchProperties().addFilter_Favorites(true)
        )
        navController!!.navigate(R.id.navigation_list)
    }

    fun onCafeClick(view: View) {
        viewModelMain.ourSearchProperties_update(
            initialSearchProperties().updateType(CAFE_TYPE)
        )
        navController!!.navigate(R.id.navigation_list)
    }

    fun onFastClick(view: View) {
        viewModelMain.ourSearchProperties_update(
            initialSearchProperties().updateType(FASTFOOD_TYPE)
        )
        navController!!.navigate(R.id.navigation_list)
    }

    override fun onStart() {
        super.onStart()
    }
}
