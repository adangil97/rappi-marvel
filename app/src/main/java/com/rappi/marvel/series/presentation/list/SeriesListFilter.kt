package com.rappi.marvel.series.presentation.list

/**
 * @author Adán Castillo.
 */
data class SeriesListFilter(
    val page: Int,
    val requireMoreData: Boolean = false
)
