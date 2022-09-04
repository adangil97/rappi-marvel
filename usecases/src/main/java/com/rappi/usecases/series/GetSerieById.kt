package com.rappi.usecases.series

import com.rappi.data.series.repositories.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Obtiene una serie por id.
 *
 * @author Adán Castillo.
 */
class GetSerieById(
    private val seriesRepository: SeriesRepository,
    private val getSerieDescription: GetSerieDescription
) {

    suspend operator fun invoke(id: Int) = withContext(Dispatchers.IO) {
        val serie = seriesRepository.getSerieById(id)
        serie.copy(
            description = serie.description ?: getSerieDescription(serie.urlDescription)
        )
    }
}