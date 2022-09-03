package com.rappi.data.series.repositories

import com.rappi.data.DataConstants
import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.data.series.datasources.SeriesRemoteDataSource
import com.rappi.domain.series.dto.SerieDto

/**
 * Realiza las operaciones remotas y locales necesarias para series marvel.
 *
 * @author Adán Castillo.
 */
class SeriesRepository(
    private val seriesRemoteDataSource: SeriesRemoteDataSource,
    private val seriesLocalDataSource: SeriesLocalDataSource
) {

    /**
     * Obtiene un listado de series paginado.
     */
    suspend fun getSeries(pageNumber: Int): List<SerieDto> {
        /*
            Obtenemos mediante la pagina el desplazamiento que interpreta la api.
            Por ejemplo
                Pagina 0: offset = 0 * 20 = 0
                Pagina 1: offset = 1 * 20 = 20
                Pagina 2: offset = 2 * 20 = 40

            Así las capas superiores ya solo deben entregar una pagina incrementable.
        */
        val offset = pageNumber * DataConstants.PAGE_SIZE
        // Obtenemos las series de la api.
        val remoteSeries = seriesRemoteDataSource.getSeries(offset)
        // Las integramos a la base de datos local.
        seriesLocalDataSource.insertSeries(remoteSeries)
        // Devolvemos el listado de las series locales.
        return seriesLocalDataSource.getSeries(offset, DataConstants.PAGE_SIZE)
    }

    /**
     * Obtiene todas las series locales.
     */
    suspend fun getAllSeries() = seriesLocalDataSource.getAllSeries()

    /**
     * Busca series en local.
     *
     * @param query [String] cadena a buscar en los titulos de todas las series.
     */
    suspend fun searchSeries(query: String) = seriesLocalDataSource.searchSeries(query)

    /**
     * Obtiene una serie mediante id.
     *
     * @param id [Int] identificador de la serie.
     */
    suspend fun getSerieById(id: Int) = seriesLocalDataSource.getSerieById(id)
}