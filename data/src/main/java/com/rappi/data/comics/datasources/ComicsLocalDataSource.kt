package com.rappi.data.comics.datasources

import com.rappi.domain.comics.dto.ComicDto

/**
 * @author Adán Castillo.
 */
interface ComicsLocalDataSource {

    suspend fun getComics(offset: Int, limit: Int): List<ComicDto>

    suspend fun insertComics(vararg comics: ComicDto)

    suspend fun searchComics(query: String): List<ComicDto>
}