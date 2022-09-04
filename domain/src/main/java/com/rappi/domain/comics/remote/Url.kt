package com.rappi.domain.comics.remote

import kotlinx.serialization.Serializable

@Serializable
data class Url(
    val type: String,
    val url: String
)