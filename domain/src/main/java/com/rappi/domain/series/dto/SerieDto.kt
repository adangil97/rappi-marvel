package com.rappi.domain.series.dto

/**
 * Es el modelo de series marvel a transferir a las capas superiores.
 *
 * @author Adán Castillo.
 */
open class SerieDto(
    open val id: Int,
    open val title: String,
    open val description: String,
    open val urlImage: String,
    open val time: Long
)
