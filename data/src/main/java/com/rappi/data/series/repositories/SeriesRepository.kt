package com.rappi.data.series.repositories

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
    companion object {
        private const val PAGE_SIZE = 20
    }

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
        val offset = pageNumber * PAGE_SIZE
        return try {
            // Obtenemos las series de la api.
            val remoteSeries = seriesRemoteDataSource.getSeries(offset)
            // Las integramos a la base de datos local.
            seriesLocalDataSource.insertSeries(*remoteSeries.toTypedArray())
            // Devolvemos el listado de las series locales.
            seriesLocalDataSource.getSeries(offset, PAGE_SIZE)
        } catch (exception: Exception) {
            // Si ocurre un error devolvemos el listado de las series locales si hay.
            seriesLocalDataSource.getSeries(offset, PAGE_SIZE)
        }
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