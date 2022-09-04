package com.rappi.marvel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Contiene las llamadas a las tablas de la base de datos para entidades de tipo serie.
 *
 * @author Adán Castillo.
 */
@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: List<MarvelEntity>)

    @Query("SELECT * FROM MarvelEntity WHERE type = :type ORDER BY time DESC LIMIT :limit OFFSET :offset")
    fun getSeries(
        offset: Int,
        limit: Int,
        type: MarvelType = MarvelType.SERIES
    ): Flow<List<MarvelEntity>>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type ORDER BY time DESC")
    suspend fun getAllSeries(
        type: MarvelType = MarvelType.SERIES
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type AND title LIKE '%' || :query || '%' ORDER BY time DESC")
    suspend fun searchSeries(
        query: String,
        type: MarvelType = MarvelType.SERIES
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE id = :id")
    suspend fun getSerieById(id: Int): MarvelEntity
}