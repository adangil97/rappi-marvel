package com.rappi.data.series.datasources

import com.rappi.domain.series.Series

/**
 * @author Adán Castillo.
 */
interface SeriesLocalDataSource {

    suspend fun getSeries(): List<Series>

    suspend fun insertSeries(vararg series: Series)

    suspend fun searchSeries(query: String): List<Series>
}