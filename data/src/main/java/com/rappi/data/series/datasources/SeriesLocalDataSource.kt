package com.rappi.data.series.datasources

import com.rappi.domain.series.dto.SerieDto

/**
 * @author Adán Castillo.
 */
interface SeriesLocalDataSource {

    suspend fun getSeries(): List<SerieDto>

    suspend fun insertSeries(vararg series: SerieDto)

    suspend fun searchSeries(query: String): List<SerieDto>
}