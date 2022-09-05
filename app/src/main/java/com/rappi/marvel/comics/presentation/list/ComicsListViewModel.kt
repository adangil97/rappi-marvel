package com.rappi.marvel.comics.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rappi.data.utils.Resource
import com.rappi.usecases.comics.GetAllComics
import com.rappi.usecases.comics.GetComics
import com.rappi.usecases.comics.SearchComics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Realiza la comunicación con los casos de uso, también informa a UI sobre acciones, estados y efectos.
 *
 * @author Adán Castillo.
 */
@HiltViewModel
class ComicsListViewModel @Inject constructor(
    private val getComics: GetComics,
    private val searchComics: SearchComics,
    private val getAllComics: GetAllComics
) : ViewModel() {
    private val mSideEffect: MutableSharedFlow<ComicsListState> = MutableSharedFlow()
    val sideEffect = mSideEffect.asSharedFlow()

    fun onEvent(event: ComicsListEvent) {
        when (event) {
            is ComicsListEvent.OnSearchComics -> {
                if (event.query.isNotEmpty()) {
                    onSearchComics(event.query)
                } else {
                    onGetAllComics()
                }
            }
            is ComicsListEvent.OnGetComics -> onGetComics(event.page)
        }
    }

    private fun onGetComics(page: Int) {
        viewModelScope.launch {
            getComics(page)
                .distinctUntilChanged()
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            mSideEffect.emit(ComicsListState.ShowLoading)
                        }
                        is Resource.Success -> {
                            mSideEffect.emit(
                                ComicsListState.ShowComics(
                                    resource.data.comics.map {
                                        ComicsAdapterItemType.ComicDtoType(it)
                                    }
                                )
                            )
                        }
                        is Resource.Error -> {
                            if (resource.data.page == 0) {
                                mSideEffect.emit(
                                    ComicsListState.ShowPlaceholderError(
                                        resource.throwable.localizedMessage
                                            ?: resource.throwable.message
                                            ?: "Unknown Error"
                                    )
                                )
                            } else {
                                mSideEffect.emit(
                                    ComicsListState.ShowGenericError(
                                        resource.throwable.localizedMessage
                                            ?: resource.throwable.message
                                            ?: "Unknown Error"
                                    )
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun onGetAllComics() {
        viewModelScope.launch {
            val comics = getAllComics()
            if (comics.isNotEmpty())
                mSideEffect.emit(
                    ComicsListState.ShowSearchComics(
                        comics.map {
                            ComicsAdapterItemType.ComicDtoType(it)
                        }
                    )
                )
            else
                mSideEffect.emit(
                    ComicsListState.ShowEmpty
                )
        }
    }

    private fun onSearchComics(query: String) {
        viewModelScope.launch {
            val comics = searchComics(query)
            if (comics.isNotEmpty())
                mSideEffect.emit(
                    ComicsListState.ShowSearchComics(
                        comics.map {
                            ComicsAdapterItemType.ComicDtoType(it)
                        }
                    )
                )
            else
                mSideEffect.emit(
                    ComicsListState.ShowEmpty
                )
        }
    }
}