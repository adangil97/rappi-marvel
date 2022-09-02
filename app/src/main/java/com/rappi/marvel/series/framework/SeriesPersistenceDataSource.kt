package com.rappi.marvel.series.framework

import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.domain.series.dto.SerieDto
import com.rappi.marvel.database.SeriesDao
import com.rappi.marvel.utils.toMarvelEntity
import com.rappi.marvel.utils.toSerieDto

/**
 * @author Adán Castillo.
 */
class SeriesPersistenceDataSource(
    private val seriesDao: SeriesDao
) : SeriesLocalDataSource {

    override suspend fun getSeries(offset: Int, limit: Int): List<SerieDto> =
        seriesDao.getSeries(offset, limit).map {
            it.toSerieDto()
        }

    override suspend fun getAllSeries(): List<SerieDto> =
        seriesDao.getAllSeries().map {
            it.toSerieDto()
        }

    override suspend fun insertSeries(vararg series: SerieDto) =
        seriesDao.insert(
            *series.map {
                it.toMarvelEntity()
            }.toTypedArray()
        )

    override suspend fun searchSeries(query: String): List<SerieDto> =
        seriesDao.searchSeries(query).map {
            it.toSerieDto()
        }

    override suspend fun getSerieById(id: Int): SerieDto =
        seriesDao.getSerieById(id).toSerieDto()
}