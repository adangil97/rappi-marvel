package com.rappi.marvel.utils

import com.rappi.domain.comics.dto.ComicDto
import com.rappi.domain.series.dto.SerieDto
import com.rappi.marvel.database.MarvelEntity
import com.rappi.marvel.database.MarvelType

/**
 * Contiene funciones de conversión de modelos transferibles a modelos de base de datos.
 *
 * @author Adán Castillo.
 */

fun MarvelEntity.toSerieDto(): SerieDto =
    SerieDto(
        id = id,
        title = title,
        description = description,
        urlImage = urlImage,
        time = time,
        lastUpdate = lastUpdate
    )

fun SerieDto.toMarvelEntity(): MarvelEntity =
    MarvelEntity(
        id = id,
        title = title,
        description = description,
        urlImage = urlImage,
        type = MarvelType.SERIES,
        time = time,
        lastUpdate = lastUpdate
    )

fun MarvelEntity.toComicDto(): ComicDto =
    ComicDto(
        id = id,
        title = title,
        description = description,
        urlImage = urlImage,
        time = time,
        lastUpdate = lastUpdate
    )

fun ComicDto.toMarvelEntity(): MarvelEntity =
    MarvelEntity(
        id = id,
        title = title,
        description = description,
        urlImage = urlImage,
        type = MarvelType.COMICS,
        time = time,
        lastUpdate = lastUpdate
    )

fun Int.toHexColor(): String =
    String.format("%06X", 0xFFFFFF and this)