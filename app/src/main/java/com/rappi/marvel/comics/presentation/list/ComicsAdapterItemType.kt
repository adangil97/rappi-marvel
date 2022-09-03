package com.rappi.marvel.comics.presentation.list

import com.rappi.domain.comics.dto.ComicDto

/**
 * @author Adán Castillo.
 */
sealed class ComicsAdapterItemType {

    data class ComicDtoType(
        val comicDto: ComicDto
    ) : ComicsAdapterItemType()

    object ComicLoadingType : ComicsAdapterItemType()
}