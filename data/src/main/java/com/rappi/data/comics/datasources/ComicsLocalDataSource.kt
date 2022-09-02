package com.rappi.data.comics.datasources

import com.rappi.domain.comics.dto.ComicDto

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
    suspend fun getComics(offset: Int, limit: Int): List<ComicDto>

    /**
     * Obtiene todos los comics disponibles en local.
     */
    suspend fun getAllComics(): List<ComicDto>

    /**
     * Integra todas los comics.
     *
     * @param comics [ComicDto] array de los comics a insertar.
     */
    suspend fun insertComics(vararg comics: ComicDto)

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
}