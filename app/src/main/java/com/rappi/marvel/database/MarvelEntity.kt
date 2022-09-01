package com.rappi.marvel.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Adán Castillo.
 */
@Entity
data class MarvelEntity(
    @PrimaryKey
    var id: Int = 0,
    val title: String,
    val description: String,
    val type: MarvelType
)