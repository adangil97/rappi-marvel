package com.rappi.marvel.comics.presentation.detail

import android.graphics.drawable.Drawable

/**
 * Definición de contrato de los diferentes eventos que se pueden realizar en el detalle de comic marvel.
 *
 * @author Adán Castillo.
 */
sealed class ComicsDetailEvent {

    data class OnGetComicById(
        val id: Int
    ) : ComicsDetailEvent()

    data class OnCalculateDominantColor(
        val drawable: Drawable
    ) : ComicsDetailEvent()
}