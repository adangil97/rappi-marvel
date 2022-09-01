package com.rappi.marvel.utils

import com.rappi.domain.comics.dto.ComicDto
import com.rappi.domain.series.dto.SerieDto
import com.rappi.marvel.database.MarvelEntity
import com.rappi.marvel.database.MarvelType

/**
 * @author Adán Castillo.
 */

fun MarvelEntity.toSerieDto(): SerieDto =
    SerieDto(
        id = id,
        title = title,
        description = description
    )

fun SerieDto.toMarvelEntity(): MarvelEntity =
    MarvelEntity(
        id = id,
        title = title,
        description = description,
        type = MarvelType.SERIES
    )

fun MarvelEntity.toComicDto(): ComicDto =
    ComicDto(
        id = id,
        title = title,
        description = description
    )

fun ComicDto.toMarvelEntity(): MarvelEntity =
    MarvelEntity(
        id = id,
        title = title,
        description = description,
        type = MarvelType.COMICS
    )