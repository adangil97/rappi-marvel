package com.rappi.usecases.comics

import com.rappi.data.comics.repositories.ComicsRepository
import com.rappi.usecases.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Obtiene un comic por id.
 *
 * @author Adán Castillo.
 */
class GetComicById(
    private val comicsRepository: ComicsRepository,
    private val getComicDescription: GetComicDescription
) {

    suspend operator fun invoke(id: Int) = withContext(Dispatchers.IO) {
        val comic = comicsRepository.getComicById(id)
        comic.copy(
            description = comic.description ?: getComicDescription(comic.urlDescription)
        )
    }
}