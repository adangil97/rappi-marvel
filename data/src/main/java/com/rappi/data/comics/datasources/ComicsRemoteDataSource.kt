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
 * Contrato de definición de las llamadas remotas necesarias para comics marvel.
 *
 * @param client [HttpClient] es el cliente que realizará las llamadas a la api.
 *
 * @author Adán Castillo.
 */
abstract class ComicsRemoteDataSource(
    private val client: HttpClient
) {

    /**
     * Obtiene un listado de comics desde el servicio.
     *
     * @param offset [Int] es el desplazamiento a realizar.
     */
    suspend fun getComics(offset: Int): List<ComicDto> {
        return try {
            val response: ComicDataWrapper = client.get("comics") {
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
                parameter(
                    "orderBy",
                    "-modified"
                )
                parameter(
                    "limit",
                    DataConstants.PAGE_SIZE
                )
            }.body()
            response.data.results.map {
                // Convierte los comics obtenidas del servicio a un listado de comics transferible a capas superiores.
                it.toComicDto()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emptyList()
        }
    }
}