package com.gb.rating.ui.settings

import android.os.Bundle

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.*
import com.gb.rating.MainActivity
import com.gb.rating.R
import com.gb.rating.models.OurSearchPropertiesValue
import com.gb.rating.models.countDistance
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : PreferenceFragmentCompat() {
    private val settingsViewModel : SettingsViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_settings, rootKey)
        initListerners()
    }

    private fun takeOurSearchPropertiesValue() = (activity as MainActivity).viewModelMain.ourSearchPropertiesValue()
    private fun updateOurSearchProperties(intValue : OurSearchPropertiesValue) = (activity as MainActivity).viewModelMain.ourSearchProperties_update(intValue)

    private fun initListerners() {

        //настройки страны
        val chooseCountry: DropDownPreference? =
            findPreference(activity?.resources!!.getString(R.string.COUNTRY_KEY))
        chooseCountry?.setOnPreferenceChangeListener { _, newValue ->
            updateOurSearchProperties(
                takeOurSearchPropertiesValue().updateCountry(newValue as String)
            )
            true
        }

        //настройки города
        val chooseCity: DropDownPreference? =
            findPreference(activity?.resources!!.getString(R.string.CITY_KEY))
        chooseCity?.setOnPreferenceChangeListener { _, newValue ->
            updateOurSearchProperties(
                takeOurSearchPropertiesValue().updateCity(newValue as String)
            )
            true
        }

        //работа с настройками расстояния до объекта
        val filterDistance: SwitchPreferenceCompat? = findPreference(activity?.resources!!.getString(R.string.DISTANCEFILTER_KEY))
        val limitDistance: SeekBarPreference? = findPreference(activity?.resources!!.getString(R.string.DISTANCE_KEY))
        val listener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val realDistance: Double = countDistance(newValue)
            limitDistance?.summary = prepareDistanceTitle(realDistance)
            updateOurSearchProperties(
                takeOurSearchPropertiesValue().updateDistance((realDistance))
            )
            true
        }

        filterDistance?.onPreferenceChangeListener = listener
        limitDistance?.onPreferenceChangeListener = listener
        val realDistance: Double = countDistance()
        limitDistance?.summary = prepareDistanceTitle(realDistance)

    }

    private fun prepareDistanceTitle(realDistance: Double) =
        if (realDistance > 0) {
            "Не далее ${realDistance} км."
        } else {
            " Любая дальность"
        }
}