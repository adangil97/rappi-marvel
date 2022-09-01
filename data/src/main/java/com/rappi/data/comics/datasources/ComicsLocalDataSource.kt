package com.rappi.data.comics.datasources

import com.rappi.domain.comics.dto.ComicDto

/**
 * @author Adán Castillo.
 */
interface ComicsLocalDataSource {

    suspend fun getComics(): List<ComicDto>

    suspend fun insertComics(vararg comics: ComicDto)

    suspend fun searchComics(query: String): List<ComicDto>
}