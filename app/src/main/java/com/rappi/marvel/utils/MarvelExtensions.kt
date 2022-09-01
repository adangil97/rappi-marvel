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
        description = description,
        urlImage = urlImage
    )

fun SerieDto.toMarvelEntity(): MarvelEntity =
    MarvelEntity(
        id = id,
        title = title,
        description = description,
        urlImage = urlImage,
        type = MarvelType.SERIES,
        time = System.currentTimeMillis()
    )

fun MarvelEntity.toComicDto(): ComicDto =
    ComicDto(
        id = id,
        title = title,
        description = description,
        urlImage = urlImage
    )

fun ComicDto.toMarvelEntity(): MarvelEntity =
    MarvelEntity(
        id = id,
        title = title,
        description = description,
        urlImage = urlImage,
        type = MarvelType.COMICS,
        time = System.currentTimeMillis()
    )