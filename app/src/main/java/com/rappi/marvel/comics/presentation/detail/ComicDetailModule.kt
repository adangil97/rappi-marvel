package com.rappi.marvel.comics.presentation.detail

import com.rappi.data.comics.repositories.ComicsRepository
import com.rappi.usecases.comics.GetCharactersByIdComic
import com.rappi.usecases.comics.GetComicById
import com.rappi.usecases.comics.GetComicDescription
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Proporciona los modulos necesarios para la capa de presentación.
 *
 * @author Adán Castillo.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ComicDetailModule {

    /**
     * Proporciona el caso de uso de obtención de comic por id.
     */
    @Provides
    @ViewModelScoped
    fun providesGetComicById(
        comicsRepository: ComicsRepository,
        getComicDescription: GetComicDescription,
        getCharactersByIdComic: GetCharactersByIdComic
    ): GetComicById =
        GetComicById(
            comicsRepository,
            getComicDescription,
            getCharactersByIdComic
        )

    /**
     * Proporciona el cas de uso de obtención de descripción de comic.
     */
    @Provides
    @ViewModelScoped
    fun providesGetComicDescription(comicsRepository: ComicsRepository): GetComicDescription =
        GetComicDescription(comicsRepository)

    /**
     * Proporciona el caso de uso de obtención de personajes por id de comic.
     */
    @Provides
    @ViewModelScoped
    fun providesGetCharactersByIdComic(comicsRepository: ComicsRepository): GetCharactersByIdComic =
        GetCharactersByIdComic(comicsRepository)
}