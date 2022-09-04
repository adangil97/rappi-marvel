package com.rappi.marvel.series.framework

import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.data.series.datasources.SeriesRemoteDataSource
import com.rappi.data.series.repositories.SeriesRepository
import com.rappi.marvel.database.CharacterDao
import com.rappi.marvel.database.MarvelDatabase
import com.rappi.marvel.database.SeriesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.*

/**
 * Proporciona los modulos necesarios para el framework de series marvel.
 *
 * @author Adán Castillo.
 */
@Module
@InstallIn(ViewModelComponent::class)
object SeriesFrameworkModule {

    /**
     * Proporciona una instancia del acceso a la tabla de series.
     *
     * @param marvelDatabase [MarvelDatabase] es la base de datos de marvel.
     */
    @Provides
    @ViewModelScoped
    fun providesSeriesDao(marvelDatabase: MarvelDatabase): SeriesDao =
        marvelDatabase.seriesDao()

    /**
     * Proporciona una instancia de la implmentación del flujo de datos remotos.
     *
     * @param client [HttpClient] cliente necesario para hacer las llamadas a api de marvel.
     */
    @Provides
    @ViewModelScoped
    fun providesSeriesRemoteDataSource(client: HttpClient): SeriesRemoteDataSource =
        SeriesApiDataSource(client)

    /**
     * Proporciona una instancia de la implementación del flujo de datos locales.
     *
     * @param seriesDao [SeriesDao] contiene acceso a la tabla de series marvel.
     */
    @Provides
    @ViewModelScoped
    fun providesSeriesLocalDataSource(
        seriesDao: SeriesDao,
        characterDao: CharacterDao
    ): SeriesLocalDataSource =
        SeriesPersistenceDataSource(
            seriesDao,
            characterDao
        )

    /**
     * Proporciona una instancia del repositorio de llamadas a flujos de series marvel.
     */
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