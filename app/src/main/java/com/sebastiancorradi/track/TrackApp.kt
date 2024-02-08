package com.sebastiancorradi.track

import android.app.Application
import android.provider.Settings
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sebastiancorradi.track.domain.AllowForegroundUseCase
import com.sebastiancorradi.track.domain.AllowTrackingClicked
import com.sebastiancorradi.track.domain.service.CreateNotificationChannelUseCase
import com.sebastiancorradi.track.domain.service.CreateNotificationUseCase
import com.sebastiancorradi.track.domain.db.DeleteLocationsUseCase
import com.sebastiancorradi.track.domain.db.GetDBLocationsUseCase
import com.sebastiancorradi.track.domain.PermissionRequestUseCase
import com.sebastiancorradi.track.domain.db.SaveLocationUseCase
import com.sebastiancorradi.track.domain.service.StartTrackingUseCase
import com.sebastiancorradi.track.domain.service.StopTrackingUseCase
import com.sebastiancorradi.track.domain.map.UpdateFocusOnLastPositionUseCase
import com.sebastiancorradi.track.domain.map.ZoomEnabledUseCase
import com.sebastiancorradi.track.repository.DBConnection
import com.sebastiancorradi.track.repository.LocationRepository
import com.sebastiancorradi.track.store.UserStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
