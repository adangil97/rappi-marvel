package com.rappi.marvel.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Base de datos de la aplicación.
 *
 * @author Adán Castillo.
 */
@Database(
    entities = [
        MarvelEntity::class
    ],
    version = MarvelDatabase.DATABASE_VERSION
)
@TypeConverters(MarvelTypeConverter::class)
abstract class MarvelDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "marvel_database"
    }

    abstract fun comicsDao(): ComicsDao

    abstract fun seriesDao(): SeriesDao
}