package com.rappi.marvel.comics.presentation.detail

import android.graphics.drawable.Drawable

/**
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