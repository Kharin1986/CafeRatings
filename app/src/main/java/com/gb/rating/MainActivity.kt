package com.gb.rating

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.gb.rating.models.BAR_TYPE
import com.gb.rating.models.CAFE_TYPE
import com.gb.rating.models.FASTFOOD_TYPE
import com.gb.rating.models.RESTAURANT_TYPE
import com.gb.rating.ui.KeepStateNavigator
import com.gb.rating.ui.ViewModelMain
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    val viewModelMain: ViewModelMain by viewModel()
    val mKeepStateNavigator by lazy {
        KeepStateNavigator(
            this,
            supportFragmentManager,
            R.id.nav_host_fragment
        )
    }
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val result = mKeepStateNavigator.navigate(item.itemId)
            viewModelMain.processFragmentSelection(item)
            return@OnNavigationItemSelectedListener result == null
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initNavControllerAndActionBar()

        PreferenceManager.setDefaultValues(this, R.xml.main_settings, false)
    }


    private fun initNavControllerAndActionBar() {
//        setSupportActionBar(toolbar)
        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        mKeepStateNavigator.navigate(R.id.navigation_home);
    }

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
            viewModelMain.ourSearchPropertiesValue().deleteAllFilters().addFilter_Favorites(true).addFilter_RatingMoreOrEquel(
                4.5f
            )
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

    fun navigateToHome(intRemove: Boolean = false) {
        val navController = findNavController(R.id.nav_host_fragment)
        mKeepStateNavigator.navigate(R.id.navigation_home, intRemove)
    }

    fun navigateToTtem(intItem: Int) {
        val navController = findNavController(R.id.nav_host_fragment)
        mKeepStateNavigator.navigate(intItem)
    }

    override fun onStart() {
        super.onStart()
    }

}
