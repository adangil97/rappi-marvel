package com.rappi.marvel.series.presentation.list

import com.rappi.data.series.repositories.SeriesRepository
import com.rappi.usecases.series.GetSeries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * @author Adán Castillo.
 */
@Module
@InstallIn(ViewModelComponent::class)
object SeriesListModule {

    @Provides
    @ViewModelScoped
    fun providesGetSeries(seriesRepository: SeriesRepository): GetSeries =
        GetSeries(seriesRepository)
}