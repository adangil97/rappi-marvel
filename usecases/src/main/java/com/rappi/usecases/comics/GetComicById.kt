package com.rappi.usecases.comics

import com.rappi.data.comics.repositories.ComicsRepository
import com.rappi.domain.characters.dto.ModelDtoWrapper
import com.rappi.domain.comics.dto.ComicDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Obtiene un comic por id.
 *
 * @author Adán Castillo.
 */
class GetComicById(
    private val comicsRepository: ComicsRepository,
    private val getComicDescription: GetComicDescription,
    private val getCharactersByIdComic: GetCharactersByIdComic
) {

    suspend operator fun invoke(id: Int): ModelDtoWrapper<ComicDto> = withContext(Dispatchers.IO) {
        val comicAsync = async { comicsRepository.getComicById(id) }
        val charactersAsync = async { getCharactersByIdComic(id) }
        var comic = comicAsync.await()
        val characters = charactersAsync.await()
        comic = comic.copy(
            description = comic.description ?: getComicDescription(comic.urlDescription)
        )
        ModelDtoWrapper(
            comic,
            characters
        )
    }
}