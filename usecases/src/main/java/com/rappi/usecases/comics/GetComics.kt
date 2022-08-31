package com.rappi.usecases.comics

import com.rappi.data.comics.repositories.ComicsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Adán Castillo.
 */
class GetComics(
    private val comicsRepository: ComicsRepository
) {

    suspend operator fun invoke(page: Int) = withContext(Dispatchers.IO) {
        comicsRepository.getComics(page)
    }
}