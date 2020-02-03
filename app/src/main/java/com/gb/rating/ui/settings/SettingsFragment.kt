package com.gb.rating.ui.settings

import android.os.Bundle

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.gb.rating.MainActivity
import com.gb.rating.R


class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var settingsViewModel: SettingsViewModel


//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        settingsViewModel =
//            ViewModelProviders.of(this).get(SettingsViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_settings, container, false)
//        val textView: TextView = root.findViewById(R.id.text_settings)
//        settingsViewModel.text.observe(this, Observer {
//            //textView.text = it
//        })
//
//        val crash: Button = root.findViewById(R.id.crashButton)
//        crash.setOnClickListener {
//            throw RuntimeException("Test Crash") // Force a crash
//        }
//
//        return root
//    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_settings, rootKey)
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        settingsViewModel.text.observe(this, Observer {
            //textView.text = it
        })



        // Get the switch preference
        val chooseCity: DropDownPreference? = findPreference(activity?.resources!!.getString(R.string.CITY_FIELD))

        // Switch preference change listener
        chooseCity?.setOnPreferenceChangeListener{_, newValue ->
            (activity as MainActivity).viewModelMain?.ourSearchProperties?.value = (activity as MainActivity).viewModelMain?.ourSearchProperties?.value?.updateCity(newValue as String)?.updateAction(RELOAD_DATABASE_ACTION)
            true
        }
    }
}