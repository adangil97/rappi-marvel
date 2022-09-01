package com.rappi.marvel.database

import androidx.room.TypeConverter

/**
 * @author Adán Castillo.
 */
class MarvelTypeConverter {

    @TypeConverter
    fun toTypeHistoryModel(value: String) = enumValueOf<MarvelType>(value)

    @TypeConverter
    fun fromTypeHistoryModel(value: MarvelType) = value.name
}