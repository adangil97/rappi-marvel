package com.rappi.usecases.series

import com.rappi.data.comics.repositories.ComicsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Adán Castillo.
 */
class SearchSeries(
    private val seriesRepository: ComicsRepository
) {

    suspend operator fun invoke(query: String) = withContext(Dispatchers.IO) {
        seriesRepository.searchComics(query)
    }
}