package com.rappi.domain.series.remote

import kotlinx.serialization.Serializable

@Serializable
data class SeriesDataContainer(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Series>,
    val total: Int
)