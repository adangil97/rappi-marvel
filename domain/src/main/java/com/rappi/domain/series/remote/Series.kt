package com.rappi.domain.series.remote

import kotlinx.serialization.Serializable

@Serializable
data class Series(
    val description: String?,
    val endYear: Int,
    val id: Int,
    val modified: String,
    val rating: String,
    val resourceURI: String,
    val startYear: Int,
    val thumbnail: Thumbnail,
    val title: String,
    val type: String
)