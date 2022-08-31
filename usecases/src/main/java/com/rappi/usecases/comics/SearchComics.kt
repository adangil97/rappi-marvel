package com.rappi.usecases.comics

import com.rappi.data.comics.repositories.ComicsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Adán Castillo.
 */
class SearchComics(
    private val comicsRepository: ComicsRepository
) {

    suspend operator fun invoke(query: String) = withContext(Dispatchers.IO){
        comicsRepository.searchComics(query)
    }
}