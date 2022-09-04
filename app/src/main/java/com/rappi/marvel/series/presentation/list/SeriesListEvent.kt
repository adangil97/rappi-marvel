package com.rappi.marvel.series.presentation.list

/**
 * Definición de contrato de los diferentes eventos que se pueden realizar en el listado de series.
 *
 * @author Adán Castillo.
 */
sealed class SeriesListEvent {

    data class OnSearchSeries(
        val query: String
    ) : SeriesListEvent()

    object OnClearSideEffect : SeriesListEvent()
}