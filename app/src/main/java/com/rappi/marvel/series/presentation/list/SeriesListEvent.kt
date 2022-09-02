package com.rappi.marvel.series.presentation.list

/**
 * @author Adán Castillo.
 */
sealed class SeriesListEvent {

    data class OnGetSeries(
        val page: Int = 0
    ) : SeriesListEvent()

    data class OnSearchSeries(
        val query: String
    ) : SeriesListEvent()

    object OnClearSideEffect : SeriesListEvent()
}