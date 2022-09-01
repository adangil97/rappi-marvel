package com.rappi.marvel.comics.presentation

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
 * @author Adán Castillo.
 */
@HiltViewModel
class ComicsViewModel @Inject constructor(
    private val getComics: GetComics,
    private val searchComics: SearchComics,
    private val getAllComics: GetAllComics
) : ViewModel() {
    private val mSideEffect = MutableLiveData<ComicsState?>()
    val sideEffect: LiveData<ComicsState?> get() = mSideEffect

    fun onEvent(event: ComicsEvent) {
        when (event) {
            is ComicsEvent.OnGetComics -> onGetComics(event.page)
            ComicsEvent.OnClearSideEffect -> mSideEffect.value = null
            is ComicsEvent.OnSearchComics -> {
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
                mSideEffect.value = ComicsState.ShowSearchComics(comics)
            else
                mSideEffect.value = ComicsState.ShowEmpty
        }
    }

    private fun onSearchComics(query: String) {
        viewModelScope.launch {
            val comics = searchComics(query)
            if (comics.isNotEmpty())
                mSideEffect.value = ComicsState.ShowSearchComics(comics)
            else
                mSideEffect.value = ComicsState.ShowEmpty
        }
    }

    private fun onGetComics(page: Int) {
        viewModelScope.launch {
            try {
                val comics = getComics(page)
                if (comics.isEmpty() && page == 0)
                    mSideEffect.value = ComicsState.ShowEmpty
                else
                    mSideEffect.value = ComicsState.ShowComics(comics)
            } catch (exception: Exception) {
                exception.printStackTrace()
                mSideEffect.value = ComicsState.ShowGenericError(
                    exception.localizedMessage ?: exception.message ?: "Unknown Error"
                )
            }
        }
    }
}