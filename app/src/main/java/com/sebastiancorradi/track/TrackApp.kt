package com.sebastiancorradi.track

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TrackApp: Application() {

    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
    }
}