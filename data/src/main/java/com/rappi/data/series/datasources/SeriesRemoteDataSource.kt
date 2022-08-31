package com.rappi.data.series.datasources

import com.rappi.domain.series.Series
import com.rappi.domain.series.SeriesDataWrapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * @author Adán Castillo.
 */
abstract class SeriesRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getSeries(offset: Int): List<Series> {
        val response: SeriesDataWrapper = client.get("series") {
            parameter("offset", offset)
        }.body()
        return response.data.results
    }
}