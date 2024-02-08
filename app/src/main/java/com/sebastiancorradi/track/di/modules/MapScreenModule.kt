package com.sebastiancorradi.track.di.modules

import com.sebastiancorradi.track.domain.map.UpdateFocusOnLastPositionUseCase
import com.sebastiancorradi.track.domain.map.ZoomEnabledUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapScreenModule {

    @Provides
    fun provideUpdateFocusOnLastPositionUseCase() = UpdateFocusOnLastPositionUseCase()
    @Provides
    fun provideZoomEnabledUseCase() = ZoomEnabledUseCase()
}