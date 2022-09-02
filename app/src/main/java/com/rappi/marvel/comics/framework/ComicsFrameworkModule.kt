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
 * Proporciona los modulos necesarios para el framework de comics marvel.
 *
 * @author Adán Castillo.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ComicsFrameworkModule {

    /**
     * Proporciona una instancia del acceso a la tabla de comics.
     *
     * @param marvelDatabase [MarvelDatabase] es la base de datos de marvel.
     */
    @Provides
    @ViewModelScoped
    fun providesComicsDao(marvelDatabase: MarvelDatabase): ComicsDao =
        marvelDatabase.comicsDao()

    /**
     * Proporciona una instancia de la implmentación del flujo de datos remotos.
     *
     * @param client [HttpClient] cliente necesario para hacer las llamadas a api de marvel.
     */
    @Provides
    @ViewModelScoped
    fun providesComicsRemoteDataSource(client: HttpClient): ComicsRemoteDataSource =
        ComicsApiDataSource(client)

    /**
     * Proporciona una instancia de la implementación del flujo de datos locales.
     *
     * @param comicsDao [ComicsDao] contiene acceso a la tabla de comics marvel.
     */
    @Provides
    @ViewModelScoped
    fun providesComicsLocalDataSource(comicsDao: ComicsDao): ComicsLocalDataSource =
        ComicsPersistenceDataSource(comicsDao)

    /**
     * Proporciona una instancia del repositorio de llamadas a flujos de comics marvel.
     */
    @Provides
    @ViewModelScoped
    fun providesComicsRepository(
        comicsRemoteDataSource: ComicsRemoteDataSource,
        comicsLocalDataSource: ComicsLocalDataSource
    ): ComicsRepository =
        ComicsRepository(
            comicsRemoteDataSource,
            comicsLocalDataSource
        )
}