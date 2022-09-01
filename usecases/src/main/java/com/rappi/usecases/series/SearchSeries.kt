package com.rappi.usecases.series

import com.rappi.data.series.repositories.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Adán Castillo.
 */
class SearchSeries(
    private val seriesRepository: SeriesRepository
) {

    suspend operator fun invoke(query: String) = withContext(Dispatchers.IO) {
        seriesRepository.searchSeries(query)
    }
}