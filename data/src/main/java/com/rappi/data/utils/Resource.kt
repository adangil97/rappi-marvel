package com.rappi.data.utils

/**
 * @author Adán Castillo.
 */
sealed class Resource<T> {

    data class Loading<T>(val data: T?) : Resource<T>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(val throwable: Throwable, val data: T) : Resource<T>()
}
