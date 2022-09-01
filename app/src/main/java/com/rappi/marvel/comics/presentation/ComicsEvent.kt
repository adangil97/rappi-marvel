package com.rappi.marvel.comics.presentation

/**
 * @author Adán Castillo.
 */
sealed class ComicsEvent {

    data class OnGetComics(
        val page: Int = 0
    ) : ComicsEvent()
}
