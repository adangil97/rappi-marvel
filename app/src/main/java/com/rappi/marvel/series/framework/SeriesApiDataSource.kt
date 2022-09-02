package com.rappi.marvel.series.framework

import com.rappi.data.series.datasources.SeriesRemoteDataSource
import io.ktor.client.*

/**
 * Es la implementación del contrato de Series Remotas (para este caso solo se encarga de proporcionar el cliente).
 *
 * @param client [HttpClient] cliente necesario para hacer las llamadas a la api de marvel.
 *
 * @author Adán Castillo.
 */
class SeriesApiDataSource(client: HttpClient) : SeriesRemoteDataSource(client)