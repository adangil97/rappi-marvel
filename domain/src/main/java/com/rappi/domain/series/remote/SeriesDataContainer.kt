package com.rappi.domain.series.remote

data class SeriesDataContainer(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Series>,
    val total: Int
)