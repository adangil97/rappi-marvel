package com.rappi.domain.comics.dto

/**
 * Es el modelo de comics marvel a transferir a capas superiores.
 *
 * @author Adán Castillo.
 */
data class ComicDto(
    val id: Int,
    val title: String,
    val description: String,
    val urlImage: String
)
