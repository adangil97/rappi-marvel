package com.rappi.marvel.series.presentation

import com.rappi.domain.series.dto.SerieDto

/**
 * @author Adán Castillo.
 */
sealed class SeriesState {

    data class ShowSeries(
        val series: List<SerieDto>
    ) : SeriesState()

    data class ShowSearchSeries(
        val series: List<SerieDto>
    ) : SeriesState()

    object ShowEmpty : SeriesState()

    data class ShowGenericError(
        val errorMessage: String
    ) : SeriesState()
}
