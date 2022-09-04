package com.rappi.domain

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDataContainer<T>(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<T>,
    val total: Int
)