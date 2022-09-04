package com.rappi.usecases.comics

import com.rappi.data.comics.repositories.ComicsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Obtiene el listado de personjaes por id de comic.
 *
 * @author Adán Castillo.
 */
class GetCharactersByIdComic(
    private val comicsRepository: ComicsRepository
) {

    suspend operator fun invoke(idComic: Int) = withContext(Dispatchers.IO) {
        comicsRepository.getCharactersByIdComic(idComic)
    }
}