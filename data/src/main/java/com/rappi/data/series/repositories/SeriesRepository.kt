package com.rappi.data.series.repositories

import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.data.series.datasources.SeriesRemoteDataSource
import com.rappi.domain.series.Series

/**
 * @author Adán Castillo.
 */
class SeriesRepository(
    private val seriesRemoteDataSource: SeriesRemoteDataSource,
    private val seriesLocalDataSource: SeriesLocalDataSource
) {
    companion object {
        private const val PAGE_SIZE = 20
    }

    suspend fun getSeries(pageNumber: Int): List<Series> {
        val offset = pageNumber * PAGE_SIZE
        return try {
            val remoteSeries = seriesRemoteDataSource.getSeries(offset)
            seriesLocalDataSource.insertSeries(*remoteSeries.toTypedArray())
            seriesLocalDataSource.getSeries()
        } catch (exception: Exception) {
            seriesLocalDataSource.getSeries()
        }
    }

    suspend fun searchSeries(query: String) = seriesLocalDataSource.searchSeries(query)
}