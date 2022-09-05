package com.rappi.marvel.comics.presentation.list

/**
 * @author Adán Castillo.
 */
data class ComicListFilter(
    val page: Int,
    val requireMoreData: Boolean = false
)
