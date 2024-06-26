package com.rappi.marvel.series.presentation.list

import com.rappi.data.series.repositories.SeriesRepository
import com.rappi.usecases.series.GetAllSeries
import com.rappi.usecases.series.GetSeries
import com.rappi.usecases.series.SearchSeries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Proporciona los modulos necesarios para la capa de presentación.
 *
 * @author Adán Castillo.
 */
@Module
@InstallIn(ViewModelComponent::class)
object SeriesListModule {

    /**
     * Proporciona el caso de uso de obtención de series paginadas.
     */
    @Provides
    @ViewModelScoped
    fun providesGetSeries(seriesRepository: SeriesRepository): GetSeries =
        GetSeries(seriesRepository)

    /**
     * Proporciona el caso de uso de buscar series.
     */
    @Provides
    @ViewModelScoped
    fun providesSearchSeries(seriesRepository: SeriesRepository): SearchSeries =
        SearchSeries(seriesRepository)

    /**
     * Proporciona el caso de uso de obtener todas las series locales.
     */
    @Provides
    @ViewModelScoped
    fun providesGetAllSeries(seriesRepository: SeriesRepository): GetAllSeries =
        GetAllSeries(seriesRepository)
}