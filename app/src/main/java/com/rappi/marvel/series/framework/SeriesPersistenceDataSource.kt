package com.rappi.marvel.series.framework

import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.domain.series.dto.SerieDto
import com.rappi.domain.series.remote.Series

/**
 * @author Adán Castillo.
 */
class SeriesPersistenceDataSource : SeriesLocalDataSource {

    override suspend fun getSeries(): List<SerieDto> {
        TODO("Not yet implemented")
    }

    override suspend fun insertSeries(vararg series: SerieDto) {
        TODO("Not yet implemented")
    }

    override suspend fun searchSeries(query: String): List<SerieDto> {
        TODO("Not yet implemented")
    }
}