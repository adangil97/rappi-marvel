package com.rappi.usecases.series

import com.rappi.data.series.repositories.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Adán Castillo.
 */
class GetCharactersByIdSerie(
    private val seriesRepository: SeriesRepository
) {

    suspend operator fun invoke(idSerie: Int) = withContext(Dispatchers.IO) {
        seriesRepository.getCharactersByIdSerie(idSerie)
    }
}