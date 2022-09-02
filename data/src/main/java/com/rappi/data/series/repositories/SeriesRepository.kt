package com.rappi.data.series.repositories

import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.data.series.datasources.SeriesRemoteDataSource
import com.rappi.domain.series.dto.SerieDto

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

    suspend fun getSeries(pageNumber: Int): List<SerieDto> {
        val offset = pageNumber * PAGE_SIZE
        return try {
            val remoteSeries = seriesRemoteDataSource.getSeries(offset)
            seriesLocalDataSource.insertSeries(*remoteSeries.toTypedArray())
            seriesLocalDataSource.getSeries(offset, PAGE_SIZE)
        } catch (exception: Exception) {
            seriesLocalDataSource.getSeries(offset, PAGE_SIZE)
        }
    }

    suspend fun getAllSeries() = seriesLocalDataSource.getAllSeries()

    suspend fun searchSeries(query: String) = seriesLocalDataSource.searchSeries(query)

    suspend fun getSerieById(id: Int) = seriesLocalDataSource.getSerieById(id)
}