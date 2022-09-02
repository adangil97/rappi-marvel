package com.rappi.marvel.series.presentation.list

import com.rappi.domain.series.dto.SerieDto

/**
 * @author Adán Castillo.
 */
sealed class SeriesListState {

    data class ShowSeries(
        val series: List<SerieDto>
    ) : SeriesListState()

    data class ShowSearchSeries(
        val series: List<SerieDto>
    ) : SeriesListState()

    object ShowEmpty : SeriesListState()

    data class ShowGenericError(
        val errorMessage: String
    ) : SeriesListState()
}
