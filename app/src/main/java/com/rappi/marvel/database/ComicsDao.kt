package com.rappi.marvel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Contiene las llamadas a las tablas de la base de datos para entidades de tipo comic.
 *
 * @author Adán Castillo.
 */
@Dao
interface ComicsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: List<MarvelEntity>)

    @Query("SELECT * FROM MarvelEntity WHERE type = :type ORDER BY time DESC LIMIT :limit OFFSET :offset")
    suspend fun getComics(
        offset: Int,
        limit: Int,
        type: MarvelType = MarvelType.COMICS
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type ORDER BY time DESC")
    suspend fun getAllComics(
        type: MarvelType = MarvelType.COMICS
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE type = :type AND title LIKE '%' || :query || '%' ORDER BY time DESC")
    suspend fun searchComics(
        query: String,
        type: MarvelType = MarvelType.COMICS
    ): List<MarvelEntity>

    @Query("SELECT * FROM MarvelEntity WHERE id = :id")
    suspend fun getComicById(id: Int): MarvelEntity
}