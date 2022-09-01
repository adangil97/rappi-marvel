package com.rappi.marvel.comics.presentation.list

import com.rappi.data.comics.repositories.ComicsRepository
import com.rappi.usecases.comics.GetComics
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
object ComicListModule {

    @Provides
    @ViewModelScoped
    fun providesGetComics(comicsRepository: ComicsRepository): GetComics =
        GetComics(comicsRepository)
}