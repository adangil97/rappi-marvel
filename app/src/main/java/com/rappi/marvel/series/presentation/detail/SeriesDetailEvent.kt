package com.rappi.marvel.series.presentation.detail

import android.graphics.drawable.Drawable

/**
 * Definición de contrato de los diferentes eventos que se pueden realizar en el detalle de serie marvel.
 *
 * @author Adán Castillo.
 */
sealed class SeriesDetailEvent {

    data class OnGetSerieById(
        val id: Int
    ) : SeriesDetailEvent()

    data class OnCalculateDominantColor(
        val drawable: Drawable
    ) : SeriesDetailEvent()
}