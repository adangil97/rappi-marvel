package com.rappi.domain.comics.remote

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val extension: String,
    val path: String
)