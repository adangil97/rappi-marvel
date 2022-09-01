package com.rappi.domain.comics.remote

data class ComicDataWrapper(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val data: ComicDataContainer,
    val etag: String,
    val status: String
)