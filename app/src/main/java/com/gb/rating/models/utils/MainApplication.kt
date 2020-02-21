package com.gb.rating.models.utils

import android.app.Application
import android.content.Context
import com.gb.rating.di.appModule
import com.gb.rating.di.listFragment
import com.gb.rating.di.main
import org.koin.core.context.startKoin


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
            modules(listOf(appModule, main, listFragment))
        }
        //startKoin(this, listOf(appModule))
    }
}