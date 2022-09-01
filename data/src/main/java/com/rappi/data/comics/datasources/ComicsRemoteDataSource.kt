package com.rappi.data.comics.datasources

import com.rappi.data.DataConstants
import com.rappi.data.utils.toComicDto
import com.rappi.data.utils.toMD5
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.domain.comics.remote.ComicDataWrapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * @author Adán Castillo.
 */
abstract class ComicsRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getComics(offset: Int): List<ComicDto> {
        return try {
            val response: ComicDataWrapper = client.get("comics") {
                val currentTime = System.currentTimeMillis()
                val strHash = "$currentTime${DataConstants.PRIVATE_KEY}${DataConstants.PUBLIC_KEY}"
                val hash = strHash.toMD5()
                parameter("offset", offset)
                parameter("ts", currentTime)
                parameter("apikey", DataConstants.PUBLIC_KEY)
                parameter(
                    "hash",
                    hash
                )
                parameter(
                    "orderBy",
                    "modified"
                )
            }.body()
            response.data.results.map {
                it.toComicDto()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emptyList()
        }
    }
}