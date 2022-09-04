package com.rappi.marvel.series.framework

import com.rappi.data.series.datasources.SeriesLocalDataSource
import com.rappi.domain.series.dto.SerieDto
import com.rappi.marvel.database.SeriesDao
import com.rappi.marvel.utils.toMarvelEntity
import com.rappi.marvel.utils.toSerieDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Es la implementación del contrato de Series locales.
 *
 * @param seriesDao [SeriesDao] contiene accesos a la tabla de series de marvel.
 *
 * @author Adán Castillo.
 */
class SeriesPersistenceDataSource(
    private val seriesDao: SeriesDao
) : SeriesLocalDataSource {

    override fun getSeries(offset: Int, limit: Int): Flow<List<SerieDto>> =
        seriesDao.getSeries(offset, limit).map {
            it.map { marvelEntity ->
                marvelEntity.toSerieDto()
            }
        }

    override suspend fun getAllSeries(): List<SerieDto> =
        seriesDao.getAllSeries().map {
            it.toSerieDto()
        }

    override suspend fun insertSeries(series: List<SerieDto>) =
        seriesDao.insert(
            series.map {
                it.toMarvelEntity()
            }
        )

    override suspend fun searchSeries(query: String): List<SerieDto> =
        seriesDao.searchSeries(query).map {
            it.toSerieDto()
        }

    override suspend fun getSerieById(id: Int): SerieDto =
        seriesDao.getSerieById(id).toSerieDto()
}