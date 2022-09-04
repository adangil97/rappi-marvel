package com.rappi.marvel.comics.presentation.list

/**
 * Definición de contrato de los diferentes efectos que podemos mostrar en UI de listado de comics marvel.
 *
 * @author Adán Castillo.
 */
sealed class ComicsListState {

    data class ShowComics(
        val comics: List<ComicsAdapterItemType.ComicDtoType>
    ) : ComicsListState()

    data class ShowSearchComics(
        val comics: List<ComicsAdapterItemType.ComicDtoType>
    ) : ComicsListState()

    object ShowEmpty : ComicsListState()

    data class ShowGenericError(
        val errorMessage: String
    ) : ComicsListState()

    data class ShowPlaceholderError(
        val errorMessage: String
    ) : ComicsListState()

    object ShowLoading : ComicsListState()
}
