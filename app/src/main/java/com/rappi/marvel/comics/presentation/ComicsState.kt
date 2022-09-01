package com.rappi.marvel.comics.presentation

import com.rappi.domain.comics.dto.ComicDto

/**
 * @author Adán Castillo.
 */
sealed class ComicsState {

    data class ShowComics(
        val comics: List<ComicDto>
    ) : ComicsState()

    data class ShowSearchComics(
        val comics: List<ComicDto>
    ) : ComicsState()

    object ShowEmpty : ComicsState()

    data class ShowGenericError(
        val errorMessage: String
    ) : ComicsState()
}
