package com.rappi.domain.characters.remote

import com.rappi.domain.Thumbnail
import kotlinx.serialization.Serializable

/**
 * @author Adán Castillo.
 */
@Serializable
data class Character(
    val id: Int,
    val name: String,
    val thumbnail: Thumbnail
)
