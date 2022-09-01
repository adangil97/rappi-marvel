package com.rappi.marvel.series.presentation

import com.rappi.domain.series.remote.Series

/**
 * @author Adán Castillo.
 */
sealed class SeriesState {

    data class ShowSeries(
        val series: List<Series>
    ) : SeriesState()

    data class ShowGenericError(
        val errorMessage: String
    ) : SeriesState()
}
