package com.sebastiancorradi.track.di.modules

import com.sebastiancorradi.track.domain.db.DeleteLocationsUseCase
import com.sebastiancorradi.track.domain.db.GetDBLocationsUseCase
import com.sebastiancorradi.track.domain.db.SaveLocationUseCase
import com.sebastiancorradi.track.repository.DBConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    fun provideDBConnection() = DBConnection()

    @Provides
    fun provideDeleteLocationsUseCase(dbConnection: DBConnection) = DeleteLocationsUseCase(dbConnection)

    @Provides
    fun provideSaveLocationUseCase(dbConnection: DBConnection,
    ) = SaveLocationUseCase(dbConnection)

    @Provides
    fun provideGetDBLocationsUseCase(dbConnection: DBConnection,
    ) = GetDBLocationsUseCase(dbConnection)


}