package com.rappi.marvel.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Es una representación tanto del modelo comic como serie, para base de datos.
 *
 * @author Adán Castillo.
 */
@Entity
data class MarvelEntity(
    @PrimaryKey
    var id: Int = 0,
    val title: String,
    val description: String,
    val urlImage: String,
    val type: MarvelType,
    val urlDescription: String?,
    val time: Long,
    val lastUpdate: Long
)