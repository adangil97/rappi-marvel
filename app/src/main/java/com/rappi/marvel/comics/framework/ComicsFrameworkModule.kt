package com.rappi.marvel.comics.framework

import com.rappi.data.comics.datasources.ComicsLocalDataSource
import com.rappi.data.comics.datasources.ComicsRemoteDataSource
import com.rappi.data.comics.repositories.ComicsRepository
import com.rappi.marvel.database.ComicsDao
import com.rappi.marvel.database.MarvelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.*

/**
 * @author Adán Castillo.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ComicsFrameworkModule {

    @Provides
    @ViewModelScoped
    fun providesComicsDao(marvelDatabase: MarvelDatabase): ComicsDao =
        marvelDatabase.comicsDao()

    @Provides
    @ViewModelScoped
    fun providesComicsRemoteDataSource(client: HttpClient): ComicsRemoteDataSource =
        ComicsApiDataSource(client)

    @Provides
    @ViewModelScoped
    fun providesComicsLocalDataSource(comicsDao: ComicsDao): ComicsLocalDataSource =
        ComicsPersistenceDataSource(comicsDao)

    @Provides
    @ViewModelScoped
    fun provides(
        comicsRemoteDataSource: ComicsRemoteDataSource,
        comicsLocalDataSource: ComicsLocalDataSource
    ): ComicsRepository =
        ComicsRepository(
            comicsRemoteDataSource,
            comicsLocalDataSource
        )
}