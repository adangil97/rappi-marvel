package com.rappi.data.comics.datasources

import com.rappi.domain.comics.Comic
import com.rappi.domain.comics.ComicDataWrapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * @author Adán Castillo.
 */
abstract class ComicsRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getComics(offset: Int): List<Comic> {
        val response: ComicDataWrapper = client.get("comics") {
            parameter("offset", offset)
        }.body()
        return response.data.results
    }
}