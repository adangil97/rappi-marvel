package com.rappi.marvel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author Adán Castillo.
 */
@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: MarvelEntity)

    @Query("SELECT * FROM MarvelEntity WHERE type = :type")
    suspend fun getSeries(type: MarvelType = MarvelType.SERIES): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type AND title LIKE '%' || :query || '%'")
    suspend fun searchSeries(
        query: String,
        type: MarvelType = MarvelType.SERIES
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE id = :id")
    suspend fun getSeriesById(id: Int): MarvelEntity
}