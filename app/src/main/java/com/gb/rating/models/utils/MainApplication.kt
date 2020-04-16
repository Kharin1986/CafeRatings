package com.gb.rating.models.utils

import android.app.Application
import android.content.Context
import com.gb.rating.di.*
import com.gb.rating.googleMapsAPI.UpdateDatabase
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin

const val STARTING_LOAD_FROM_GOOGLE_MAP_API = true

class MainApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = MainApplication.applicationContext()

        startKoin {
            // declare modules
            modules(listOf(appModule, main, listFragment, cafeInfo, home, review, search, settings ))
        }

        val db : FirebaseDatabase by inject()
        db.setPersistenceEnabled(true)
        CommonAuthFunctions.checkAuth()

        if (STARTING_LOAD_FROM_GOOGLE_MAP_API) UpdateDatabase.doIt()
    }
}