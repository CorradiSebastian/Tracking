package com.sebastiancorradi.track

import android.app.Application
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sebastiancorradi.track.domain.AllowForegroundUseCase
import com.sebastiancorradi.track.domain.AllowTrackingClicked
import com.sebastiancorradi.track.domain.PermissionRequestUseCase
import com.sebastiancorradi.track.domain.StartTrackingUseCase
import com.sebastiancorradi.track.domain.StopTrackingUseCase
import com.sebastiancorradi.track.repository.LocationRepository
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

        @Provides
        fun provideLocationRepository(
            fusedLocationProviderClient: FusedLocationProviderClient
        ) = LocationRepository(fusedLocationProviderClient)

        @Provides
        fun provideAllowForegroundUseCase(
        ) = AllowForegroundUseCase()

        @Provides
        fun provideAllowTrackingClicked(
        ) = AllowTrackingClicked()

        @Provides
        fun providePermissionRequestUseCase(
        ) = PermissionRequestUseCase()

        @Provides
        fun provideStartTrackingUseCase(locationRepository: LocationRepository
        ) = StartTrackingUseCase(locationRepository)

        @Provides
        fun provideStopTrackingUseCase(locationRepository: LocationRepository
        ) = StopTrackingUseCase(locationRepository)
    }
}
