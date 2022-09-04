package com.rappi.usecases.series

import com.rappi.data.series.repositories.SeriesRepository
import com.rappi.domain.characters.dto.ModelDtoWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Obtiene una serie por id.
 *
 * @author Adán Castillo.
 */
class GetSerieById(
    private val seriesRepository: SeriesRepository,
    private val getSerieDescription: GetSerieDescription,
    private val getCharactersByIdSerie: GetCharactersByIdSerie
) {

    suspend operator fun invoke(id: Int) = withContext(Dispatchers.IO) {
        val serieAsync = async { seriesRepository.getSerieById(id) }
        val charactersAsync = async { getCharactersByIdSerie(id) }
        var serie = serieAsync.await()
        val characters = charactersAsync.await()
        serie = serie.copy(
            description = serie.description ?: getSerieDescription(serie.urlDescription)
        )
        ModelDtoWrapper(
            serie,
            characters
        )
    }
}