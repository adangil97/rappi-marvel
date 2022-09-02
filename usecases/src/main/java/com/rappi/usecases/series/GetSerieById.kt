package com.rappi.usecases.series

import com.rappi.data.series.repositories.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Adán Castillo.
 */
class GetSerieById(
    private val seriesRepository: SeriesRepository
) {

    suspend operator fun invoke(id: Int) = withContext(Dispatchers.IO) {
        seriesRepository.getSerieById(id)
    }
}