package com.rappi.domain.comics.remote

import kotlinx.serialization.Serializable

@Serializable
data class Comic(
    val description: String?,
    val diamondCode: String,
    val digitalId: Int,
    val ean: String,
    val format: String,
    val id: Int,
    val isbn: String,
    val issn: String,
    val issueNumber: Int,
    val modified: String,
    val pageCount: Int,
    val resourceURI: String,
    val thumbnail: Thumbnail,
    val title: String,
    val upc: String,
    val variantDescription: String,
)