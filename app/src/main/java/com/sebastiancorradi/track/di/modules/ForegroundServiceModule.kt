package com.sebastiancorradi.track.di.modules

import com.sebastiancorradi.track.domain.AllowForegroundUseCase
import com.sebastiancorradi.track.domain.PermissionRequestUseCase
import com.sebastiancorradi.track.domain.db.SaveLocationUseCase
import com.sebastiancorradi.track.domain.service.CreateNotificationChannelUseCase
import com.sebastiancorradi.track.domain.service.CreateNotificationUseCase
import com.sebastiancorradi.track.domain.service.StartTrackingUseCase
import com.sebastiancorradi.track.domain.service.StopTrackingUseCase
import com.sebastiancorradi.track.repository.LocationRepository
import com.sebastiancorradi.track.store.UserStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ForegroundServiceModule {

    @Provides
    fun providePermissionRequestUseCase(
    ) = PermissionRequestUseCase()

    @Provides
    fun provideStartTrackingUseCase(locationRepository: LocationRepository,
    ) = StartTrackingUseCase(locationRepository)

    @Provides
    fun provideStopTrackingUseCase(locationRepository: LocationRepository, saveLocationUseCase: SaveLocationUseCase, dataStore: UserStore
    ) = StopTrackingUseCase(locationRepository, saveLocationUseCase, dataStore)

    @Provides
    fun provideCreateNotificationUseCase() = CreateNotificationUseCase()

    @Provides
    fun provideCreateNotificationChannelUseCase() = CreateNotificationChannelUseCase()

    @Provides
    fun provideAllowForegroundUseCase(
    ) = AllowForegroundUseCase()

}