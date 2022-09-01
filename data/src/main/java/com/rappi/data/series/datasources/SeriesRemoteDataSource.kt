package com.rappi.data.series.datasources

import com.rappi.data.DataConstants
import com.rappi.domain.series.Series
import com.rappi.domain.series.SeriesDataWrapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.security.MessageDigest

/**
 * @author Adán Castillo.
 */
abstract class SeriesRemoteDataSource(
    private val client: HttpClient
) {

    suspend fun getSeries(offset: Int): List<Series> {
        val response: SeriesDataWrapper = client.get("series") {
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
        return response.data.results
    }
}