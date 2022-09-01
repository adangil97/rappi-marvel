package com.rappi.marvel.comics.framework

import com.rappi.data.comics.datasources.ComicsLocalDataSource
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.marvel.database.ComicsDao
import com.rappi.marvel.utils.toComicDto
import com.rappi.marvel.utils.toMarvelEntity

/**
 * @author Adán Castillo.
 */
class ComicsPersistenceDataSource(
    private val comicsDao: ComicsDao
) : ComicsLocalDataSource {

    override suspend fun getComics(offset: Int, limit: Int): List<ComicDto> =
        comicsDao.getComics(offset, limit).map {
            it.toComicDto()
        }

    override suspend fun getAllComics(): List<ComicDto> =
        comicsDao.getAllComics().map {
            it.toComicDto()
        }

    override suspend fun insertComics(vararg comics: ComicDto) =
        comicsDao.insert(
            *comics.map {
                it.toMarvelEntity()
            }.toTypedArray()
        )

    override suspend fun searchComics(query: String): List<ComicDto> =
        comicsDao.searchComics(query).map {
            it.toComicDto()
        }
}