package com.rappi.data.comics.repositories

import com.rappi.data.comics.datasources.ComicsLocalDataSource
import com.rappi.data.comics.datasources.ComicsRemoteDataSource
import com.rappi.domain.comics.dto.ComicDto

/**
 * @author Adán Castillo.
 */
class ComicsRepository(
    private val comicsRemoteDataSource: ComicsRemoteDataSource,
    private val comicsLocalDataSource: ComicsLocalDataSource
) {
    companion object {
        private const val PAGE_SIZE = 20
    }

    /**
     * Obtiene un listado de comics paginados.
     */
    suspend fun getComics(page: Int): List<ComicDto> {
        /*
            Obtenemos mediante la pagina el desplazamiento que interpreta la api.
            Por ejemplo
                Pagina 0: offset = 0 * 20 = 0
                Pagina 1: offset = 1 * 20 = 20
                Pagina 2: offset = 2 * 20 = 40

            Así las capas superiores ya solo deben entregar una pagina incrementable.
        */
        val offset = page * PAGE_SIZE
        return try {
            // Obtenemos los comics de la api.
            val remoteComics = comicsRemoteDataSource.getComics(offset)
            // Los integramos en la base de datos local.
            comicsLocalDataSource.insertComics(remoteComics)
            // Devolvemos el listado de comics locales.
            comicsLocalDataSource.getComics(offset, PAGE_SIZE)
        } catch (exception: Exception) {
            // Si ocurre un error devolvemos el listado de comics locales.
            comicsLocalDataSource.getComics(offset, PAGE_SIZE)
        }
    }

    /**
     * Obtiene todos los comics locales.
     */
    suspend fun getAllComics() = comicsLocalDataSource.getAllComics()

    /**
     * Busca comics en local.
     *
     * @param query [String] cadena a buscar en los titulos de todos los comics.
     */
    suspend fun searchComics(query: String) = comicsLocalDataSource.searchComics(query)

    /**
     * Obtiene un comic mediante id.
     *
     * @param id [Int] identificador del comic.
     */
    suspend fun getComicById(id: Int) = comicsLocalDataSource.getComicById(id)
}