package com.rappi.data.series.datasources

import com.rappi.domain.characters.dto.CharacterDto
import com.rappi.domain.series.dto.SerieDto
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de definición de las llamadas locales necesarias para series marvel.
 *
 * @author Adán Castillo.
 */
interface SeriesLocalDataSource {

    /**
     * Obtiene un listado de series paginados.
     *
     * @param offset [Int] desplazamiento a realizar.
     * @param limit [Int] cantidad de elementos a obtener.
     */
    fun getSeries(offset: Int, limit: Int): Flow<List<SerieDto>>

    /**
     * Obtiene todas las series disponibles en local.
     */
    suspend fun getAllSeries(): List<SerieDto>

    /**
     * Integra todas las series.
     *
     * @param series [SerieDto] array de las series a insertar.
     */
    suspend fun insertSeries(series: List<SerieDto>)

    /**
     * Busca series en local.
     *
     * @param query [String] cadena de busqueda en titulos.
     */
    suspend fun searchSeries(query: String): List<SerieDto>

    /**
     * Obtiene una serie por id.
     *
     * @param id [Int] id de la serie e obtener
     */
    suspend fun getSerieById(id: Int): SerieDto

    /**
     * Obtiene un listado de personajes por id de serie.
     *
     * @param id [Int] id de la serie.
     */
    suspend fun getCharactersByIdSerie(id: Int): List<CharacterDto>

    /**
     * Permite insertar un listado de personajes por serie.
     *
     * @param characters [List] es el listado de personajes por serie
     */
    suspend fun insertCharacters(characters: List<CharacterDto>)
}