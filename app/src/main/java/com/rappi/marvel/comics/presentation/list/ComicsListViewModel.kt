package com.rappi.marvel.comics.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rappi.data.utils.Resource
import com.rappi.usecases.comics.GetAllComics
import com.rappi.usecases.comics.GetComics
import com.rappi.usecases.comics.SearchComics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Realiza la comunicación con los casos de uso, también informa a UI sobre acciones, estados y efectos.
 *
 * @author Adán Castillo.
 */
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ComicsListViewModel @Inject constructor(
    private val getComics: GetComics,
    private val searchComics: SearchComics,
    private val getAllComics: GetAllComics
) : ViewModel() {
    private val mSideEffect = MutableLiveData<ComicsListState?>()
    val sideEffect: LiveData<ComicsListState?> get() = mSideEffect

    val pagingEvent: (Int) -> Unit
    val pagingDataFlow: Flow<ComicsListState>

    init {
        val actionStateFlow = MutableSharedFlow<Int>()
        val filteredScrolled = actionStateFlow
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )

        pagingDataFlow = filteredScrolled
            .flatMapLatest { page ->
                getComics(page)
            }.map { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        ComicsListState.ShowLoading
                    }
                    is Resource.Success -> {
                        ComicsListState.ShowComics(
                            resource.data.comics.map {
                                ComicsAdapterItemType.ComicDtoType(it)
                            }
                        )
                    }
                    is Resource.Error -> {
                        if (resource.data.page == 0) {
                            ComicsListState.ShowPlaceholderError(
                                resource.throwable.localizedMessage ?: resource.throwable.message
                                ?: "Unknown Error"
                            )
                        } else {
                            ComicsListState.ShowGenericError(
                                resource.throwable.localizedMessage ?: resource.throwable.message
                                ?: "Unknown Error"
                            )
                        }
                    }
                }
            }

        pagingEvent = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    fun onEvent(event: ComicsListEvent) {
        when (event) {
            ComicsListEvent.OnClearSideEffect -> {
                mSideEffect.value = null
            }
            is ComicsListEvent.OnSearchComics -> {
                if (event.query.isNotEmpty()) {
                    onSearchComics(event.query)
                } else {
                    onGetAllComics()
                }
            }
        }
    }

    private fun onGetAllComics() {
        viewModelScope.launch {
            val comics = getAllComics()
            if (comics.isNotEmpty())
                mSideEffect.value = ComicsListState.ShowSearchComics(
                    comics.map {
                        ComicsAdapterItemType.ComicDtoType(it)
                    }
                )
            else
                mSideEffect.value = ComicsListState.ShowEmpty
        }
    }

    private fun onSearchComics(query: String) {
        viewModelScope.launch {
            val comics = searchComics(query)
            if (comics.isNotEmpty())
                mSideEffect.value = ComicsListState.ShowSearchComics(
                    comics.map {
                        ComicsAdapterItemType.ComicDtoType(it)
                    }
                )
            else
                mSideEffect.value = ComicsListState.ShowEmpty
        }
    }
}