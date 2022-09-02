package com.rappi.usecases.comics

import com.rappi.data.comics.repositories.ComicsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Obtiene todos los comics que hay almacenados en local.
 *
 * @author Adán Castillo.
 */
class GetAllComics(
    private val comicsRepository: ComicsRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        comicsRepository.getAllComics()
    }
}