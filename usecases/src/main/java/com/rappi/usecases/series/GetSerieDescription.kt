package com.rappi.usecases.series

import com.rappi.data.series.repositories.SeriesRepository
import com.rappi.usecases.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Obtiene la descripción de una serie.
 *
 * @author Adán Castillo.
 */
class GetSerieDescription(
    private val seriesRepository: SeriesRepository
) {

    suspend operator fun invoke(urlDescription: String?): String = withContext(Dispatchers.IO) {
        urlDescription?.let { url ->
            seriesRepository.getHtmlDescription(url)
        } ?: Constants.UNKNOWN
    }
}