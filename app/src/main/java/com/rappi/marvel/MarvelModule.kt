package com.rappi.marvel

import android.content.Context
import androidx.room.Room
import com.rappi.marvel.database.MarvelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Proporciona los modulos necesarios a nivel de aplicación.
 *
 * @author Adán Castillo.
 */
@Module
@InstallIn(SingletonComponent::class)
object MarvelModule {

    /**
     * Proporciona una instancia de la base de datos de marvel.
     *
     * @param context [Context] contexto necesario para crear la bd.
     */
    @Provides
    @Singleton
    fun providesMarvelDatabase(@ApplicationContext context: Context): MarvelDatabase =
        Room.databaseBuilder(
            context,
            MarvelDatabase::class.java,
            MarvelDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    /**
     * Proporciona el cliente necesario en las capas internas para obtener datos desde la api de marvel.
     */
    @Provides
    @Singleton
    fun providesHttpClient(): HttpClient =
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                // Aplicamos un convertidor json.
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            expectSuccess = true
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    val clientException = exception as? ClientRequestException
                        ?: return@handleResponseExceptionWithRequest
                    val exceptionResponse = clientException.response
                    if (exceptionResponse.status != HttpStatusCode.OK) {
                        val exceptionResponseText = exceptionResponse.bodyAsText()
                        throw Exception(exceptionResponseText)
                    }
                }
            }
            defaultRequest {
                // Agregamos la url base del servicio.
                host = MarvelConstants.BASE_URL
                url {
                    protocol = URLProtocol.HTTPS
                }
            }
        }
}