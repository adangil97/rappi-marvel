package com.rappi.marvel.series.presentation.list

/**
 * Definición de contrato de los diferentes efectos que podemos mostrar en UI de listado de series marvel.
 *
 * @author Adán Castillo.
 */
sealed class SeriesListState {

    data class ShowSeries(
        val series: List<SeriesAdapterItemType.SerieDtoType>
    ) : SeriesListState()

    data class ShowSearchSeries(
        val series: List<SeriesAdapterItemType.SerieDtoType>
    ) : SeriesListState()

    object ShowEmpty : SeriesListState()

    data class ShowGenericError(
        val errorMessage: String
    ) : SeriesListState()

    data class ShowPlaceholderError(
        val errorMessage: String
    ) : SeriesListState()

    object ShowLoading : SeriesListState()
}
