package com.gb.rating.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ListFragment
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.gb.rating.ui.home.HomeFragment
import com.gb.rating.ui.review.QrScanFragment
import com.gb.rating.ui.search.SearchFragment

@Navigator.Name("keep_state_fragment") // `keep_state_fragment` is used in navigation xml
class KeepStateNavigator(
    private val context: Context,
    private val manager: FragmentManager, // Should pass childFragmentManager.
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {

        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()
        var removed = false
        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment

        if (currentFragment != null) {
            if (currentFragment is HomeFragment || currentFragment is com.gb.rating.ui.list.ListFragment || currentFragment is SearchFragment)
                transaction.hide(currentFragment)
            else {
                transaction.remove(currentFragment)
                removed = true}
//            transaction.detach(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            transaction.add(containerId, fragment, tag)
        } else {
            //transaction.attach(fragment)
            transaction.show(fragment)
        }

        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        if (removed) transaction.commitAllowingStateLoss()
        else transaction.commitNow()

//        if (currentFragment != null) {
//            if (!(currentFragment is HomeFragment || currentFragment is com.gb.rating.ui.list.ListFragment || currentFragment is SearchFragment))
//                manager.beginTransaction().remove(currentFragment).commitAllowingStateLoss()
//        }


        return if (initialNavigate) {
            destination
        } else {
            null
        }
    }
}