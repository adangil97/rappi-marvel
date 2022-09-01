package com.rappi.data.comics.datasources

import com.rappi.data.DataConstants
import com.rappi.data.utils.toComicDto
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.domain.comics.remote.ComicDataWrapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.security.MessageDigest

/**
 * @author Adán Castillo.
 */
abstract class ComicsRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getComics(offset: Int): List<ComicDto> {
        val response: ComicDataWrapper = client.get("comics") {
            val md = MessageDigest.getInstance("MD5")
            val currentTime = System.currentTimeMillis()
            parameter("offset", offset)
            parameter("ts", currentTime)
            parameter("apikey", DataConstants.PUBLIC_KEY)
            parameter(
                "hash",
                md.digest(
                    "$currentTime${DataConstants.PUBLIC_KEY}${DataConstants.PRIVATE_KEY}".toByteArray()
                ).toString()
            )
        }.body()
        return response.data.results.map {
            it.toComicDto()
        }
    }
}