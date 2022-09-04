package com.rappi.domain.characters.remote

import com.rappi.domain.Thumbnail

/**
 * @author Adán Castillo.
 */
data class Character(
    val id: Int,
    val name: String,
    val thumbnail: Thumbnail
)
