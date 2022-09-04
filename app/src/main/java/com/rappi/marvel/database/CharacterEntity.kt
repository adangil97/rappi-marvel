package com.rappi.marvel.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Adán Castillo.
 */
@Entity
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val urlImage: String,
    val connectionId: Int
)
