package com.rappi.data.series.datasources

import com.rappi.domain.series.dto.SerieDto

/**
 * @author Adán Castillo.
 */
interface SeriesLocalDataSource {

    suspend fun getSeries(offset: Int, limit: Int): List<SerieDto>

    suspend fun getAllSeries(): List<SerieDto>

    suspend fun insertSeries(vararg series: SerieDto)

    suspend fun searchSeries(query: String): List<SerieDto>

    suspend fun getSerieById(id: Int): SerieDto
}