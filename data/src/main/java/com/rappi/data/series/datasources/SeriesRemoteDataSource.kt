package com.rappi.data.series.datasources

import com.rappi.data.DataConstants
import com.rappi.data.utils.toMD5
import com.rappi.data.utils.toSerieDto
import com.rappi.domain.series.dto.SerieDto
import com.rappi.domain.series.remote.SeriesDataWrapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * @author Adán Castillo.
 */
abstract class SeriesRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getSeries(offset: Int): List<SerieDto> {
        return try {
            val response: SeriesDataWrapper = client.get("series") {
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
            return response.data.results.map {
                it.toSerieDto()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emptyList()
        }
    }
}