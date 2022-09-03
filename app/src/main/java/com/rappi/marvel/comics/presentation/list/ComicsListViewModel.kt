package com.rappi.marvel.comics.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rappi.usecases.comics.GetAllComics
import com.rappi.usecases.comics.GetComics
import com.rappi.usecases.comics.SearchComics
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val mSideEffect = MutableLiveData<ComicsListState?>()
    val sideEffect: LiveData<ComicsListState?> get() = mSideEffect

    private var page = 0

    fun onEvent(event: ComicsListEvent) {
        when (event) {
            ComicsListEvent.OnGetComics -> onGetComics()
            ComicsListEvent.OnClearSideEffect -> {
                page = 0
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

    private fun onGetComics() {
        viewModelScope.launch {
            try {
                // Obtenemos 3 paginas por llamada.
                val comics = getComics(page)
                if (comics.isEmpty() && page == 0)
                    mSideEffect.value = ComicsListState.ShowEmpty
                else {
                    page += 1
                    mSideEffect.value = ComicsListState.ShowComics(
                        comics.map {
                            ComicsAdapterItemType.ComicDtoType(it)
                        }
                    )
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                if (page == 0)
                    mSideEffect.value = ComicsListState.ShowPlaceholderError(
                        exception.localizedMessage ?: exception.message ?: "Unknown Error"
                    )
                else
                    mSideEffect.value = ComicsListState.ShowGenericError(
                        exception.localizedMessage ?: exception.message ?: "Unknown Error"
                    )
            }
        }
    }
}