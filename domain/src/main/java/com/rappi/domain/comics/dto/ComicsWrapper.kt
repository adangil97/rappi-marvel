package com.rappi.domain.comics.dto

/**
 * @author Adán Castillo.
 */
data class ComicsWrapper(
    val page: Int,
    val comics: List<ComicDto> = emptyList()
)
