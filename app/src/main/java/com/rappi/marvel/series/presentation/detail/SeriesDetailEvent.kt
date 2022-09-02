package com.rappi.marvel.series.presentation.detail

import android.graphics.drawable.Drawable

/**
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