package com.rappi.marvel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Contiene las llamadas a las tablas de la base de datos para entidades de tipo serie.
 *
 * @author Adán Castillo.
 */
@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg series: MarvelEntity)

    @Query("SELECT * FROM MarvelEntity WHERE type = :type ORDER BY time LIMIT :limit OFFSET :offset")
    suspend fun getSeries(
        offset: Int,
        limit: Int,
        type: MarvelType = MarvelType.SERIES
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type ORDER BY time")
    suspend fun getAllSeries(
        type: MarvelType = MarvelType.SERIES
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type AND title LIKE '%' || :query || '%' ORDER BY time")
    suspend fun searchSeries(
        query: String,
        type: MarvelType = MarvelType.SERIES
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE id = :id")
    suspend fun getSerieById(id: Int): MarvelEntity
}