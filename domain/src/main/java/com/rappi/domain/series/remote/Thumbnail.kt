package com.rappi.domain.series.remote

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val extension: String,
    val path: String
)