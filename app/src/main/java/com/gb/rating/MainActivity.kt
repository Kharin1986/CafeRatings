package com.gb.rating

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions
import com.gb.rating.ui.ViewModelMain
import com.gb.rating.ui.settings.*

class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    var viewModelMain: ViewModelMain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        initNavControllerAndActionBar()
    }

    private fun initViewModel() {
        viewModelMain = ViewModelProviders.of(this)[ViewModelMain::class.java]
        viewModelMain?.ourSearchProperties?.observe(this, Observer {
            it?.let {
                if (it.action != INITIATION_ACTION)
                    viewModelMain?.refreshCafeList()
            }
        }) //TODO: как перенести обозреватель LiveData в ViewModel ?
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
        viewModelMain?.ourSearchProperties?.value =
            initialSearchProperties().updateType(RESTAURANT_TYPE)
        navController!!.navigate(R.id.navigation_list)
    }

    fun onBarClick(view: View) {
        viewModelMain?.ourSearchProperties?.value = initialSearchProperties().updateType(BAR_TYPE)

        navController!!.navigate(R.id.navigation_list)
    }

    fun onTopClick(view: View) {
        viewModelMain?.ourSearchProperties?.value =
            initialSearchProperties().addFilter_RatingMoreOrEquel(4.5f)
        navController!!.navigate(R.id.navigation_list)
    }

    fun onFavClick(view: View) {
        viewModelMain?.ourSearchProperties?.value =
            initialSearchProperties().addFilter_Favorites(true)
        navController!!.navigate(R.id.navigation_list)
    }

    fun onCafeClick(view: View) {
        viewModelMain?.ourSearchProperties?.value = initialSearchProperties().updateType(CAFE_TYPE)
        navController!!.navigate(R.id.navigation_list)
    }

    fun onFastClick(view: View) {
        viewModelMain?.ourSearchProperties?.value =
            initialSearchProperties().updateType(FASTFOOD_TYPE)
        navController!!.navigate(R.id.navigation_list)
    }

    override fun onStart() {
        super.onStart()
        CommonAuthFunctions.checkAuth()
    }
}
