package com.rappi.marvel.comics.presentation.detail

import com.rappi.data.comics.repositories.ComicsRepository
import com.rappi.usecases.comics.GetComicById
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * @author Adán Castillo.
 */
@Module
@InstallIn(ViewModelComponent::class)
object ComicDetailModule {

    @Provides
    @ViewModelScoped
    fun providesGetComicById(comicsRepository: ComicsRepository): GetComicById =
        GetComicById(comicsRepository)
}