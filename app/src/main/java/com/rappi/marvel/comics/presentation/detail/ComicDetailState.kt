package com.rappi.marvel.comics.presentation.detail

import com.rappi.domain.comics.dto.ComicDto

/**
 * @author Adán Castillo.
 */
sealed class ComicDetailState {

    data class ShowComic(
        val comic: ComicDto
    ) : ComicDetailState()

    data class ShowDominantColor(
        val hexDominantColor: String
    ) : ComicDetailState()
}
