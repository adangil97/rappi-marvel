package com.rappi.data.utils

import com.rappi.domain.comics.dto.ComicDto
import com.rappi.domain.comics.remote.Comic
import com.rappi.domain.series.dto.SerieDto
import com.rappi.domain.series.remote.Series
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Contiene funciones de conversión de modelos.
 *
 * @author Adán Castillo.
 */

fun Comic.toComicDto(lastUpdate: Long): ComicDto =
    ComicDto(
        id = id,
        title = title,
        description = description,
        urlImage = "${thumbnail.path}.${thumbnail.extension}",
        urlDescription = urls.find {
            it.type == "detail"
        }?.url,
        time = modified.parseToDateMillis(),
        lastUpdate = lastUpdate
    )

fun Series.toSerieDto(lastUpdate: Long): SerieDto =
    SerieDto(
        id = id,
        title = title,
        description = description,
        urlImage = "${thumbnail.path}.${thumbnail.extension}",
        urlDescription = urls.find {
            it.type == "detail"
        }?.url,
        time = modified.parseToDateMillis(),
        lastUpdate = lastUpdate
    )

fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}

fun String.parseToDateMillis(pattern: String = "yyyy-MM-dd'T'kk:mm:ss-SSS"): Long {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    val date = format.parse(this)
    return date.time
}