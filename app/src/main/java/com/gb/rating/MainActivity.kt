package com.gb.rating

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.gb.rating.models.*
import com.gb.rating.ui.KeepStateNavigator
import com.gb.rating.ui.ViewModelMain
import com.gb.rating.ui.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.AccessController

class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    val viewModelMain: ViewModelMain by viewModel()
    val mKeepStateNavigator by lazy { KeepStateNavigator(this, supportFragmentManager, R.id.nav_host_fragment) }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        val result = mKeepStateNavigator.navigate(item.itemId)
        return@OnNavigationItemSelectedListener true}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initNavControllerAndActionBar()

        PreferenceManager.setDefaultValues(this, R.xml.main_settings, false)
        initViewModel()

    }

    private fun initViewModel() {
        //viewModelMain = ViewModelProvider(this).get(ViewModelMain::class.java)
        viewModelMain.toString()
    }

    private fun initNavControllerAndActionBar() {
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
////        // setup custom navigator
//        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
//        val navController = findNavController(R.id.nav_host_fragment)
//        navController.navigatorProvider += navigator
//        navController.setGraph(R.navigation.mobile_navigation)
//        //appbar and navView
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home,
//                R.id.navigation_review,
//                R.id.navigation_list,
//                R.id.navigation_search,
//                R.id.navigation_settings
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

//        setSupportActionBar(toolbar)


        nav_view.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)
        mKeepStateNavigator.navigate(R.id.navigation_home);


    }

//    object mOnNavigationItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
//        override fun onNavigationItemSelected(item: MenuItem): Boolean {
//            when (item.itemId){
//                R.id.navigation_home -> {
//                    val mFragment = HomeFragment()
//
//
//                }
//
//
//            }
//
//
//        }
//
//    }

    override fun onBackPressed() {
        finishAndRemoveTask() //TODO - перестала работать, MainApplication не закрывается
    }

    // Кнопки домашней страницы)
    fun onRestClick(view: View) {
        updateType_and_navigateToList(RESTAURANT_TYPE)
    }

    fun onBarClick(view: View) {
        updateType_and_navigateToList(BAR_TYPE)
    }

    fun onCafeClick(view: View) {
        updateType_and_navigateToList(CAFE_TYPE)
    }

    fun onFastClick(view: View) {
        updateType_and_navigateToList(FASTFOOD_TYPE)
    }

    fun onTopClick(view: View) {
        viewModelMain.ourSearchProperties_update(
            viewModelMain.ourSearchPropertiesValue().deleteAllFilters().addFilter_Favorites(true).addFilter_RatingMoreOrEquel(4.5f)
        )
        navigateToList()
    }

    fun onFavClick(view: View) {
        viewModelMain.ourSearchProperties_update(
            viewModelMain.ourSearchPropertiesValue().deleteAllFilters().addFilter_Favorites(true)
        )
        navigateToList()
    }


    private fun updateType_and_navigateToList(cafeType: String) {
        viewModelMain.ourSearchProperties_update(
            viewModelMain.ourSearchPropertiesValue().deleteAllFilters().updateType(cafeType)
        )
        navigateToList()
    }

     fun navigateToList() {
        val navController = findNavController(R.id.nav_host_fragment)
         mKeepStateNavigator.navigate(R.id.navigation_list)
    }

    fun navigateToHome() {
        val navController = findNavController(R.id.nav_host_fragment)
        mKeepStateNavigator.navigate(R.id.navigation_home)
    }

    override fun onStart() {
        super.onStart()
    }
}
