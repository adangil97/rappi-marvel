package com.rappi.domain.series.remote

import kotlinx.serialization.Serializable

@Serializable
data class Url(
    val type: String,
    val url: String
)