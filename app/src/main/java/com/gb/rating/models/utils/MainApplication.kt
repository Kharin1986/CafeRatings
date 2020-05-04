
package com.gb.rating.models.utils

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gb.rating.di.*
import com.gb.rating.models.Firebase_Auth.CommonAuthFunctions
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.core.logger.KOIN_TAG

const val STARTING_LOAD_FROM_GOOGLE_MAP_API = false

class MainApplication : Application() {
    val iMEI: String by lazy {setIMEI() }

    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun IMEI() = instance!!.iMEI
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare modules
            //modules(listOf(appModule, main, listFragment, cafeInfo, home, review, search, settings))
            modules(listOf(appModule, main, listFragment, cafeInfo, home, search, settings))
        }

        //авторизация
        val db: FirebaseDatabase by inject()
        db.setPersistenceEnabled(true)
        CommonAuthFunctions.checkAuth()

        //обновление базы точек (с загруженными списками кафе)
        UpdatePoints.getInstance().initialUpdating()

        //обновление базы кафе по сохраненной точке
        if (STARTING_LOAD_FROM_GOOGLE_MAP_API) UpdateDatabase.doIt()
    }



    override fun onTerminate() {
        super.onTerminate()
    }

    private fun setIMEI(): String {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    Log.d(KOIN_TAG, "No permission Manifest.permission.READ_PHONE_STATE")
                    return ""
                }
            }

            val tm = getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm.imei
            } else {
                return tm.deviceId
            }

        } catch (e: Exception) {Log.d(KOIN_TAG, "Не удалось определить IMEI"); return ""}
    }
}

