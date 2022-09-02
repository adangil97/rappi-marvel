package com.rappi.marvel.series.presentation.detail

import com.rappi.data.series.repositories.SeriesRepository
import com.rappi.usecases.series.GetSerieById
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
object SeriesDetailModule {

    /**
     * Proporciona el caso de uso de obtención de serie por id.
     */
    @Provides
    @ViewModelScoped
    fun providesGetSerieById(seriesRepository: SeriesRepository): GetSerieById =
        GetSerieById(seriesRepository)
}