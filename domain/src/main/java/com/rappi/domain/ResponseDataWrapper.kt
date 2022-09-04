package com.rappi.domain

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDataWrapper<T>(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val data: ResponseDataContainer<T>,
    val etag: String,
    val status: String
)