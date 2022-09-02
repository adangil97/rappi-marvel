package com.rappi.marvel.comics.presentation.list

import com.rappi.data.comics.repositories.ComicsRepository
import com.rappi.usecases.comics.GetAllComics
import com.rappi.usecases.comics.GetComics
import com.rappi.usecases.comics.SearchComics
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
object ComicListModule {

    /**
     * Proporciona el caso de uso de obtención de comics paginados.
     */
    @Provides
    @ViewModelScoped
    fun providesGetComics(comicsRepository: ComicsRepository): GetComics =
        GetComics(comicsRepository)

    /**
     * Proporciona el caso de uso de buscar comics.
     */
    @Provides
    @ViewModelScoped
    fun providesSearchComics(comicsRepository: ComicsRepository): SearchComics =
        SearchComics(comicsRepository)

    /**
     * Proporciona el caso de uso de obtener todos los comics locales.
     */
    @Provides
    @ViewModelScoped
    fun providesGetAllComics(comicsRepository: ComicsRepository): GetAllComics =
        GetAllComics(comicsRepository)
}