package com.rappi.data.comics.datasources

import com.rappi.domain.characters.dto.CharacterDto
import com.rappi.domain.comics.dto.ComicDto
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de definición de las llamadas locales necesarias para comics marvel.
 *
 * @author Adán Castillo.
 */
interface ComicsLocalDataSource {

    /**
     * Obtiene un listado de comics paginados.
     *
     * @param offset [Int] desplazamiento a realizar.
     * @param limit [Int] cantidad de elementos a obtener.
     */
    fun getComics(offset: Int, limit: Int): Flow<List<ComicDto>>

    /**
     * Obtiene todos los comics disponibles en local.
     */
    suspend fun getAllComics(): List<ComicDto>

    /**
     * Integra todas los comics.
     *
     * @param comics [ComicDto] array de los comics a insertar.
     */
    suspend fun insertComics(comics: List<ComicDto>)

    /**
     * Busca comics en local.
     *
     * @param query [String] cadena de busqueda.
     */
    suspend fun searchComics(query: String): List<ComicDto>

    /**
     * Obtiene un comic mediante id.
     *
     * @param id [Int] identificador del comic a obtener.
     */
    suspend fun getComicById(id: Int): ComicDto

    /**
     * Obtiene un listado de personajes por id de comic.
     *
     * @param id [Int] id del comic.
     */
    suspend fun getCharactersByIdComic(id: Int): List<CharacterDto>

    /**
     * Permite insertar un listado de personajes por serie.
     *
     * @param characters [List] es el listado de personajes por serie
     */
    suspend fun insertCharacters(characters: List<CharacterDto>)
}