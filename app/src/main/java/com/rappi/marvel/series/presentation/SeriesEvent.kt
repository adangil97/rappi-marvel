package com.rappi.marvel.series.presentation

/**
 * @author Adán Castillo.
 */
sealed class SeriesEvent {

    data class OnGetSeries(
        val page: Int = 0
    ) : SeriesEvent()
}