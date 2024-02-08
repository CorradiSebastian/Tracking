package com.sebastiancorradi.track.di.modules

import android.app.Application
import com.sebastiancorradi.track.store.UserStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    fun provideDataStore(application: Application) = UserStore(application)

}