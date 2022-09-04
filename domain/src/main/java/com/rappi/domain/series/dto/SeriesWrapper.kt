package com.rappi.domain.series.dto

/**
 * @author Adán Castillo.
 */
data class SeriesWrapper(
    val page: Int,
    val series: List<SerieDto> = emptyList()
)
