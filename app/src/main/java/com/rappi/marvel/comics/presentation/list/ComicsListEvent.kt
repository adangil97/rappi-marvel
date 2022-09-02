package com.rappi.marvel.comics.presentation.list

/**
 * Definición de contrato de los diferentes eventos que se pueden realizar en el listado de comics.
 *
 * @author Adán Castillo.
 */
sealed class ComicsListEvent {

    object OnGetComics : ComicsListEvent()

    data class OnSearchComics(
        val query: String
    ) : ComicsListEvent()

    object OnClearSideEffect : ComicsListEvent()
}
