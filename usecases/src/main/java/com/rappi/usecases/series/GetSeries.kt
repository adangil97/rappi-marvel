package com.rappi.usecases.series

import com.rappi.data.comics.repositories.ComicsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Adán Castillo.
 */
class GetSeries(
    private val seriesRepository: ComicsRepository
) {

    suspend operator fun invoke(page: Int) = withContext(Dispatchers.IO) {
        seriesRepository.getComics(page)
    }
}