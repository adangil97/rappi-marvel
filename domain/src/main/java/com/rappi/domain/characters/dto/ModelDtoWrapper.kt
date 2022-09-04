package com.rappi.domain.characters.dto

/**
 * @author Adán Castillo.
 */
data class ModelDtoWrapper<T>(
    val data: T,
    val characters: List<CharacterDto>
)