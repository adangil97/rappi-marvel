package com.rappi.marvel.series.presentation.list

/**
 * Definición de contrato de los diferentes eventos que se pueden realizar en el listado de series.
 *
 * @author Adán Castillo.
 */
sealed class SeriesListEvent {

    data class OnGetSeries(
        val page: Int
    ) : SeriesListEvent()

    data class OnSearchSeries(
        val query: String
    ) : SeriesListEvent()
}