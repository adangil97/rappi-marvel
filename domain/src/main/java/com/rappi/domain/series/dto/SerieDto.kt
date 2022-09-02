package com.rappi.domain.series.dto

/**
 * Es el modelo de series marvel a transferir a las capas superiores.
 *
 * @author Adán Castillo.
 */
data class SerieDto(
    val id: Int,
    val title: String,
    val description: String,
    val urlImage: String,
    val time: Long
)
