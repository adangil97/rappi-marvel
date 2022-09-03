package com.rappi.marvel.series.presentation.list

import com.rappi.domain.series.dto.SerieDto

/**
 * @author Adán Castillo.
 */
sealed class SeriesAdapterItemType {

    data class SerieDtoType(
        val serieDto: SerieDto
    ) : SeriesAdapterItemType()

    object SerieLoadingType : SeriesAdapterItemType()
}
