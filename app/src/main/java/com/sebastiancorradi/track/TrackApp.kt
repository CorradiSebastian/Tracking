package com.sebastiancorradi.track

import android.app.Application
import android.provider.Settings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TrackApp: Application() {
    //private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")
    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        DEVICE_ID = getDeviceID()
    }

    fun getDeviceID():String{
        val deviceId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        return deviceId
    }
    companion object {
        var DEVICE_ID = ""
        fun getDeviceID():String {
            return DEVICE_ID
        }
    }

}
