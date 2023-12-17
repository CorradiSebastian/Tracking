package com.sebastiancorradi.track

import android.app.Application
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class TrackApp: Application() {

    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {

        @Provides
        @Singleton
        fun provideGoogleApiAvailability() = GoogleApiAvailability.getInstance()

        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(
            application: Application
        ) = LocationServices.getFusedLocationProviderClient(application)

    }
}
