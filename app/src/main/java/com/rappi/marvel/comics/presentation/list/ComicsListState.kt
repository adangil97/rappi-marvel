package com.rappi.marvel.comics.presentation.list

import com.rappi.domain.comics.dto.ComicDto

/**
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
