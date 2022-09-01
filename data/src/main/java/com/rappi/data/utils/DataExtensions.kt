package com.rappi.data.utils

import com.rappi.domain.comics.dto.ComicDto
import com.rappi.domain.comics.remote.Comic
import com.rappi.domain.series.dto.SerieDto
import com.rappi.domain.series.remote.Series

/**
 * @author Adán Castillo.
 */

fun Comic.toComicDto(): ComicDto =
    ComicDto(
        id = id,
        title = title,
        description = description
    )

fun Series.toSerieDto(): SerieDto =
    SerieDto(
        id = id,
        title = title,
        description = description
    )