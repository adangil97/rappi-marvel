package com.rappi.data.comics.datasources

import com.rappi.domain.comics.Comic

/**
 * @author Adán Castillo.
 */
interface ComicsLocalDataSource {

    suspend fun getComics(): List<Comic>

    suspend fun insertComics(vararg comics: Comic)

    suspend fun searchComics(query: String): List<Comic>
}