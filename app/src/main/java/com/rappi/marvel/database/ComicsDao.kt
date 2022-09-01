package com.rappi.marvel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author Adán Castillo.
 */
@Dao
interface ComicsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg series: MarvelEntity)

    @Query("SELECT * FROM MarvelEntity WHERE type = :type ORDER BY time LIMIT :limit OFFSET :offset")
    suspend fun getComics(
        offset: Int,
        limit: Int,
        type: MarvelType = MarvelType.COMICS
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type ORDER BY time")
    suspend fun getAllComics(
        type: MarvelType = MarvelType.COMICS
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type AND title LIKE '%' || :query || '%' ORDER BY time")
    suspend fun searchComics(
        query: String,
        type: MarvelType = MarvelType.COMICS
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE id = :id")
    suspend fun getComicsById(id: Int): MarvelEntity
}