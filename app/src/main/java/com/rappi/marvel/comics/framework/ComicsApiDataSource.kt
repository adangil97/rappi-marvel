package com.rappi.marvel.comics.framework

import com.rappi.data.comics.datasources.ComicsRemoteDataSource
import io.ktor.client.*

/**
 * @author Adán Castillo.
 */
class ComicsApiDataSource(client: HttpClient) : ComicsRemoteDataSource(client)