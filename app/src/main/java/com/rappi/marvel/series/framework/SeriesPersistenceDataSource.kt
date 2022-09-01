package com.rappi.marvel.series.framework

import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.domain.series.Series

/**
 * @author Adán Castillo.
 */
class SeriesPersistenceDataSource : SeriesLocalDataSource {

    override suspend fun getSeries(): List<Series> {
        TODO("Not yet implemented")
    }

    override suspend fun insertSeries(vararg series: Series) {
        TODO("Not yet implemented")
    }

    override suspend fun searchSeries(query: String): List<Series> {
        TODO("Not yet implemented")
    }
}