package com.gb.rating

import android.os.Bundle
import android.view.ActionMode
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
import com.gb.rating.ui.settings.BASE_UPDATED_ACTION
import com.gb.rating.ui.settings.INITIATION_ACTION

class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        initNavControllerAndActionBar()
    }

    private fun initViewModel() {
        val viewModelMain = ViewModelProviders.of(this)[ViewModelMain::class.java]
        viewModelMain.ourSearchProperties.observe(this, Observer { it?.let {
            if (it.action != INITIATION_ACTION && it.action != BASE_UPDATED_ACTION)
                viewModelMain.refreshCafeList()
        } }) //TODO: как перенести обозреватель LiveData в ViewModel (может, использовать coorutines)?
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
        bundle.putString("arg1", "rest")
        navController!!.navigate(R.id.navigation_list,bundle)
    }
    fun onBarClick(view: View) {
        bundle.putString("arg1", "bar")
        navController!!.navigate(R.id.navigation_list,bundle)
    }
    fun onTopClick(view: View) {
        bundle.putString("arg1", "top")
        navController!!.navigate(R.id.navigation_list,bundle)
    }
    fun onFavClick(view: View) {
        bundle.putString("arg1", "fav")
        navController!!.navigate(R.id.navigation_list,bundle)
    }
    fun onCafeClick(view: View) {
        bundle.putString("arg1", "cafe")
        navController!!.navigate(R.id.navigation_list,bundle)
    }
    fun onFastClick(view: View) {
        bundle.putString("arg1", "fast")
        navController!!.navigate(R.id.navigation_list,bundle)
    }

    override fun onStart() {
        super.onStart()
        CommonAuthFunctions.checkAuth()
    }

}
