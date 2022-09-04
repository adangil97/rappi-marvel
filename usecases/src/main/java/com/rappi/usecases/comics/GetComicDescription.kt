package com.rappi.usecases.comics

import com.rappi.data.comics.repositories.ComicsRepository
import com.rappi.usecases.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Obtiene la descripción de un comic.
 *
 * @author Adán Castillo.
 */
class GetComicDescription(
    private val comicsRepository: ComicsRepository
) {

    suspend operator fun invoke(urlDescription: String?): String = withContext(Dispatchers.IO) {
        urlDescription?.let { url ->
            comicsRepository.getHtmlDescription(url)
        } ?: Constants.UNKNOWN
    }
}