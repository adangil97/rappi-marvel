package com.rappi.marvel.comics.presentation.list

/**
 * @author Adán Castillo.
 */
sealed class ComicsListEvent {

    data class OnGetComics(
        val page: Int = 0
    ) : ComicsListEvent()

    data class OnSearchComics(
        val query: String
    ) : ComicsListEvent()

    object OnClearSideEffect : ComicsListEvent()
}
