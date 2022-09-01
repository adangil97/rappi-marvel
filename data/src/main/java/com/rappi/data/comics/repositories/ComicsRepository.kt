package com.rappi.data.comics.repositories

import com.rappi.data.comics.datasources.ComicsLocalDataSource
import com.rappi.data.comics.datasources.ComicsRemoteDataSource
import com.rappi.domain.comics.dto.ComicDto

/**
 * @author Adán Castillo.
 */
class ComicsRepository(
    private val comicsRemoteDataSource: ComicsRemoteDataSource,
    private val comicsLocalDataSource: ComicsLocalDataSource
) {
    companion object {
        private const val PAGE_SIZE = 20
    }

    suspend fun getComics(page: Int): List<ComicDto> {
        val offset = page * PAGE_SIZE
        return try {
            val remoteComics = comicsRemoteDataSource.getComics(offset)
            comicsLocalDataSource.insertComics(*remoteComics.toTypedArray())
            comicsLocalDataSource.getComics(offset, PAGE_SIZE)
        } catch (exception: Exception) {
            comicsLocalDataSource.getComics(offset, PAGE_SIZE)
        }
    }

    suspend fun searchComics(query: String) = comicsLocalDataSource.searchComics(query)
}