package com.rappi.data.comics.repositories

import com.rappi.data.DataConstants
import com.rappi.data.comics.datasources.ComicsLocalDataSource
import com.rappi.data.comics.datasources.ComicsRemoteDataSource
import com.rappi.data.utils.Resource
import com.rappi.data.utils.networkBoundResource
import com.rappi.domain.characters.dto.CharacterDto
import com.rappi.domain.comics.dto.ComicsWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author Adán Castillo.
 */
class ComicsRepository(
    private val comicsRemoteDataSource: ComicsRemoteDataSource,
    private val comicsLocalDataSource: ComicsLocalDataSource
) {

    /**
     * Obtiene un listado de comics paginados.
     */
    suspend fun getComics(page: Int): Flow<Resource<ComicsWrapper>> {
        /*
            Obtenemos mediante la pagina el desplazamiento que interpreta la api.
            Por ejemplo
                Pagina 0: offset = 0 * 20 = 0
                Pagina 1: offset = 1 * 20 = 20
                Pagina 2: offset = 2 * 20 = 40

            Así las capas superiores ya solo deben entregar una pagina incrementable.
        */
        val offset = page * DataConstants.PAGE_SIZE
        return networkBoundResource(
            query = {
                comicsLocalDataSource.getComics(offset, DataConstants.PAGE_SIZE).map { comics ->
                    ComicsWrapper(page, comics)
                }
            },
            fetch = { comicsRemoteDataSource.getComics(offset) },
            saveFetchResult = { items -> comicsLocalDataSource.insertComics(items) },
            shouldFetch = { comicsWrapper ->
                comicsWrapper.comics.lastOrNull()?.let { comic ->
                    DataConstants.CACHED_TIME >= comic.lastUpdate
                } ?: true
            }
        )
    }

    /**
     * Obtiene todos los comics locales.
     */
    suspend fun getAllComics() = comicsLocalDataSource.getAllComics()

    /**
     * Busca comics en local.
     *
     * @param query [String] cadena a buscar en los titulos de todos los comics.
     */
    suspend fun searchComics(query: String) = comicsLocalDataSource.searchComics(query)

    /**
     * Obtiene un comic mediante id.
     *
     * @param id [Int] identificador del comic.
     */
    suspend fun getComicById(id: Int) = comicsLocalDataSource.getComicById(id)

    /**
     * Obtiene la descripción de un comic a traves de web scrapping.
     *
     * @param urlDescription [String] url de la descripción del comic.
     */
    fun getHtmlDescription(urlDescription: String): String? =
        comicsRemoteDataSource.getHtmlDescription(urlDescription)

    /**
     * Obtiene un listado de personajes desde la base de datos local, si no lo busca en el servicio.
     *
     * @param idComic [Int] identificador de la serie.
     */
    suspend fun getCharactersByIdComic(idComic: Int): List<CharacterDto> =
        comicsLocalDataSource.getCharactersByIdComic(idComic).ifEmpty {
            val characters = comicsRemoteDataSource.getCharactersByIdComic(idComic)
            comicsLocalDataSource.insertCharacters(characters)
            characters
        }
}