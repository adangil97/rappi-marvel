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
 * @author Adán Castillo.
 */
@Module
@InstallIn(SingletonComponent::class)
object MarvelModule {

    @Provides
    @Singleton
    fun providesMarvelDatabase(@ApplicationContext context: Context): MarvelDatabase =
        Room.databaseBuilder(
            context,
            MarvelDatabase::class.java,
            MarvelDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesHttpClient(): HttpClient =
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
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
                host = MarvelConstants.BASE_URL
                url {
                    protocol = URLProtocol.HTTPS
                }
            }
        }
}