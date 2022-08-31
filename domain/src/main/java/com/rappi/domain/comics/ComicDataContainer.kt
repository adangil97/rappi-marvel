package com.rappi.domain.comics

data class ComicDataContainer(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Comic>,
    val total: Int
)