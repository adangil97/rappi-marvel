package com.rappi.marvel.comics.framework

import com.rappi.data.comics.datasources.ComicsRemoteDataSource
import io.ktor.client.*

/**
 * Es la implementación del contrato de Comics Remotos (para este caso solo se encarga de proporcionar el cliente).
 *
 * @param client [HttpClient] cliente necesario para hacer las llamadas a la api de marvel.
 *
 * @author Adán Castillo.
 */
class ComicsApiDataSource(client: HttpClient) : ComicsRemoteDataSource(client)