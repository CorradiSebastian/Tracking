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
    }

    fun getDeviceID():String{
        val deviceId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        return deviceId
    }

}
