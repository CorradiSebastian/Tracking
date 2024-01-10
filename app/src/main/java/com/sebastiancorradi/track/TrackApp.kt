package com.sebastiancorradi.track

import android.app.Application
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sebastiancorradi.track.domain.AllowForegroundUseCase
import com.sebastiancorradi.track.domain.AllowTrackingClicked
import com.sebastiancorradi.track.domain.CreateNotificationChannelUseCase
import com.sebastiancorradi.track.domain.CreateNotificationUseCase
import com.sebastiancorradi.track.domain.GetDBLocationsUseCase
import com.sebastiancorradi.track.domain.PermissionRequestUseCase
import com.sebastiancorradi.track.domain.SaveLocationUseCase
import com.sebastiancorradi.track.domain.StartTrackingUseCase
import com.sebastiancorradi.track.domain.StopTrackingUseCase
import com.sebastiancorradi.track.repository.DBConnection
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

    fun getDeviceID():String{
        val deviceId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        return deviceId
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

        @Singleton
        @Provides
        fun provideLocationRepository(
            fusedLocationProviderClient: FusedLocationProviderClient,
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
        fun provideStartTrackingUseCase(locationRepository: LocationRepository,
        ) = StartTrackingUseCase(locationRepository)

        @Provides
        fun provideStopTrackingUseCase(locationRepository: LocationRepository,
        ) = StopTrackingUseCase(locationRepository)

        @Provides
        fun provideDBConnection() = DBConnection()

        @Provides
        fun provideSaveLocationUseCase(dbConnection: DBConnection,
                                        ) = SaveLocationUseCase(dbConnection)

        @Provides
        fun provideGetDBLocationsUseCase(dbConnection: DBConnection,
                                         ) = GetDBLocationsUseCase(dbConnection)

        @Provides
        fun provideCreateNotificationUseCase() = CreateNotificationUseCase()

        @Provides
        fun provideCreateNotificationChannelUseCase() = CreateNotificationChannelUseCase()


    }
}
