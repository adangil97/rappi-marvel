package com.rappi.marvel.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author Adán Castillo.
 */
@Dao
interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity WHERE connectionId = :idConnection")
    suspend fun getCharactersByIdConnection(idConnection: Int): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)
}