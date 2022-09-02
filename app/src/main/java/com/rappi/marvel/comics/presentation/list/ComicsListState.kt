package com.rappi.marvel.comics.presentation.list

import com.rappi.domain.comics.dto.ComicDto

/**
 * Definición de contrato de los diferentes efectos que podemos mostrar en UI de listado de comics marvel.
 *
 * @author Adán Castillo.
 */
sealed class ComicsListState {

    data class ShowComics(
        val comics: List<ComicDto>
    ) : ComicsListState()

    data class ShowSearchComics(
        val comics: List<ComicDto>
    ) : ComicsListState()

    object ShowEmpty : ComicsListState()

    data class ShowGenericError(
        val errorMessage: String
    ) : ComicsListState()
}
