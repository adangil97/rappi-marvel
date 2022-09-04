package com.rappi.data.comics.datasources

import com.rappi.data.DataConstants
import com.rappi.data.utils.toCharacterDto
import com.rappi.data.utils.toComicDto
import com.rappi.data.utils.toMD5
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.domain.ResponseDataWrapper
import com.rappi.domain.characters.dto.CharacterDto
import com.rappi.domain.characters.remote.Character
import com.rappi.domain.comics.remote.Comic
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.jsoup.Jsoup

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
        val response: ResponseDataWrapper<Comic> = client.get("comics") {
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
        val lastUpdate = System.currentTimeMillis()
        return response.data.results.map {
            // Convierte los comics obtenidas del servicio a un listado de comics transferible a capas superiores.
            it.toComicDto(lastUpdate)
        }
    }

    /**
     * Permite obtener el listado de personajes desde el servicio.
     *
     * @param idComic [Int] id del comic.
     */
    suspend fun getCharactersByIdComic(idComic: Int): List<CharacterDto> {
        val response: ResponseDataWrapper<Character> = client.get("comics/$idComic/characters") {
            val currentTime = System.currentTimeMillis()
            // Se crea un hash MD5(MarcaDeTiempo+privateKey+publicKey)
            val strHash = "$currentTime${DataConstants.PRIVATE_KEY}${DataConstants.PUBLIC_KEY}"
            val hash = strHash.toMD5()
            parameter("ts", currentTime)
            parameter("apikey", DataConstants.PUBLIC_KEY)
            parameter(
                "hash",
                hash
            )
        }.body()
        return response.data.results.map {
            it.toCharacterDto(idComic)
        }
    }

    /**
     * Permite obtener la descripción de un comic con el uso de web scraping.
     *
     * @param urlDescription [String] url donde se encuentra el html con la descripción.
     */
    fun getHtmlDescription(urlDescription: String): String? {
        val html =
            Jsoup.connect(urlDescription)
                .get()

        val target = html
            .body()
            .getElementById("page-content")?.child(0)?.child(3)?.child(1)?.child(0)?.child(1)
            ?.child(2)?.child(1)
        return target?.text()
    }
}