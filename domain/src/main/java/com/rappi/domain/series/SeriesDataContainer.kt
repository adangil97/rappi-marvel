package com.rappi.domain.series

data class SeriesDataContainer(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Series>,
    val total: Int
)