package com.rappi.domain.characters.dto

/**
 * @author Adán Castillo.
 */
data class CharacterDto(
    val id: Int,
    val name: String,
    val urlImage: String,
    val connectionId: Int
)