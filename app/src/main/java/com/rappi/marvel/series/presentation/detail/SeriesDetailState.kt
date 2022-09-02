package com.rappi.marvel.series.presentation.detail

import com.rappi.domain.series.dto.SerieDto

/**
 * Definición de contrato de los diferentes efectos que podemos mostrar en UI de detalle de serie marvel.
 *
 * @author Adán Castillo.
 */
sealed class SeriesDetailState {

    data class ShowSerie(
        val serie: SerieDto
    ) : SeriesDetailState()

    data class ShowDominantColor(
        val hexDominantColor: String
    ) : SeriesDetailState()
}
