package com.rappi.marvel.series.framework

import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.data.series.datasources.SeriesRemoteDataSource
import com.rappi.data.series.repositories.SeriesRepository
import com.rappi.marvel.database.MarvelDatabase
import com.rappi.marvel.database.SeriesDao
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
object SeriesFrameworkModule {

    @Provides
    @ViewModelScoped
    fun providesSeriesDao(marvelDatabase: MarvelDatabase): SeriesDao =
        marvelDatabase.seriesDao()

    @Provides
    @ViewModelScoped
    fun providesSeriesRemoteDataSource(client: HttpClient): SeriesRemoteDataSource =
        SeriesApiDataSource(client)

    @Provides
    @ViewModelScoped
    fun providesSeriesLocalDataSource(seriesDao: SeriesDao): SeriesLocalDataSource =
        SeriesPersistenceDataSource(seriesDao)

    @Provides
    @ViewModelScoped
    fun providesSeriesRepository(
        seriesRemoteDataSource: SeriesRemoteDataSource,
        seriesLocalDataSource: SeriesLocalDataSource
    ): SeriesRepository =
        SeriesRepository(
            seriesRemoteDataSource,
            seriesLocalDataSource
        )
}