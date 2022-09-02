package com.rappi.marvel.comics.presentation.list

/**
 * Definición de contrato de los diferentes eventos que se pueden realizar en el listado de comics.
 *
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
