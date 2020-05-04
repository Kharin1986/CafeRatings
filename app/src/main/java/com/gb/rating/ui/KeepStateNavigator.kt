package com.gb.rating.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ListFragment
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.gb.rating.R
import com.gb.rating.ui.home.HomeFragment
import com.gb.rating.ui.review.QrScanFragment
import com.gb.rating.ui.review.ReviewFragment
import com.gb.rating.ui.search.SearchFragment
import com.gb.rating.ui.settings.SettingsFragment
import java.util.zip.Inflater

@Navigator.Name("keep_state_fragment") // `keep_state_fragment` is used in navigation xml
class KeepStateNavigator(
    private val context: Context,
    private val manager: FragmentManager, // Should pass childFragmentManager.
    private val containerId: Int
)  {

    fun navigate(
        destination: Int, intRemove : Boolean = false
    ): Int? {
        val tag = destination.toString()
        val transaction = manager.beginTransaction()

        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            if (intRemove) transaction.remove(currentFragment)
            else {
                //transaction.detach(currentFragment)
                transaction.hide(currentFragment)
            }
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {

            val curClassName : String = when(destination){
                R.id.navigation_home -> HomeFragment::class.java.name
                R.id.navigation_list -> com.gb.rating.ui.list.ListFragment::class.java.name
                R.id.navigation_review -> QrScanFragment::class.java.name
                R.id.navigation_search -> SearchFragment::class.java.name
                R.id.navigation_settings -> SettingsFragment::class.java.name
                R.id.navigation_review_second_page -> ReviewFragment::class.java.name
                else -> return null
            }

            fragment = manager.fragmentFactory.instantiate(context.classLoader, curClassName)
            transaction.add(containerId, fragment, tag)
        } else {
            //transaction.attach(fragment)
            transaction.show(fragment)
        }

        transaction.setPrimaryNavigationFragment(fragment)
        if (intRemove) transaction.commitAllowingStateLoss()
        else transaction.commit()

        return if (initialNavigate) {
            destination
        } else {
            null
        }
    }
}