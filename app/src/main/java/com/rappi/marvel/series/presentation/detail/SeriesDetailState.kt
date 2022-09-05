package com.rappi.marvel.series.presentation.detail

import com.rappi.domain.characters.dto.ModelDtoWrapper
import com.rappi.domain.series.dto.SerieDto

/**
 * Definición de contrato de los diferentes efectos que podemos mostrar en UI de detalle de serie marvel.
 *
 * @author Adán Castillo.
 */
sealed class SeriesDetailState {

    data class ShowSerie(
        val modelDtoWrapper: ModelDtoWrapper<SerieDto>
    ) : SeriesDetailState()

    data class ShowDominantColor(
        val hexDominantColor: String
    ) : SeriesDetailState()

    data class ShowErrorMessage(
        val errorMessage: String
    ) : SeriesDetailState()
}
