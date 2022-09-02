package com.rappi.marvel.comics.presentation.detail

import com.rappi.domain.comics.dto.ComicDto

/**
 * Definición de contrato de los diferentes efectos que podemos mostrar en UI de detalle de comic marvel.
 *
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
