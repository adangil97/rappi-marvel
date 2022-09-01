package com.rappi.marvel.series.framework

import com.rappi.data.series.datasources.SeriesRemoteDataSource
import io.ktor.client.*

/**
 * @author Adán Castillo.
 */
class SeriesApiDataSource(client: HttpClient) : SeriesRemoteDataSource(client)