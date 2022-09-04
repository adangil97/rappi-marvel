package com.rappi.data.series.datasources

import com.rappi.data.DataConstants
import com.rappi.data.utils.toMD5
import com.rappi.data.utils.toSerieDto
import com.rappi.domain.ResponseDataWrapper
import com.rappi.domain.series.dto.SerieDto
import com.rappi.domain.series.remote.Series
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.jsoup.Jsoup

/**
 * Contrato de definición de las llamadas remotas necesarias para series marvel.
 *
 * @param client [HttpClient] es el cliente que realizará las llamadas a la api.
 *
 * @author Adán Castillo.
 */
abstract class SeriesRemoteDataSource(
    private val client: HttpClient
) {

    /**
     * Obtiene un listado de series desde el servicio.
     *
     * @param offset [Int] es el desplazamiento a realizar.
     */
    suspend fun getSeries(offset: Int): List<SerieDto> {
        val response: ResponseDataWrapper<Series> = client.get("series") {
            val currentTime = System.currentTimeMillis()
            // Se crea un hash MD5(MarcaDeTiempo+privateKey+publicKey)
            val strHash = "$currentTime${DataConstants.PRIVATE_KEY}${DataConstants.PUBLIC_KEY}"
            val hash = strHash.toMD5()
            parameter("offset", offset)
            parameter("ts", currentTime)
            parameter("apikey", DataConstants.PUBLIC_KEY)
            parameter(
                "hash",
                hash
            )
            // Indicamos ordenamiento por fecha de modificación.
            parameter(
                "orderBy",
                "-modified"
            )
            parameter(
                "limit",
                DataConstants.PAGE_SIZE
            )
        }.body()
        val lastUpdate = System.currentTimeMillis()
        return response.data.results.map {
            // Convierte las series obtenidas del servicio a un listado de series transferible a capas superiores.
            it.toSerieDto(lastUpdate)
        }
    }

    fun getHtmlDescription(urlDescription: String): String? {
        val html =
            Jsoup.connect(urlDescription)
                .get()

        val target = html
            .body()
            .getElementById("page-content")?.child(0)?.child(3)?.child(1)?.child(0)?.child(1)?.child(2)?.child(1)
        return target?.text()
    }
}