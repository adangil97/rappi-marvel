package com.rappi.marvel.comics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rappi.usecases.comics.GetComics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Adán Castillo.
 */
@HiltViewModel
class ComicsViewModel @Inject constructor(
    private val getComics: GetComics
) : ViewModel() {
    private val mSideEffect = MutableLiveData<ComicsState>()
    val sideEffect: LiveData<ComicsState> get() = mSideEffect

    fun onEvent(event: ComicsEvent) {
        when (event) {
            is ComicsEvent.OnGetComics -> onGetComics(event.page)
        }
    }

    private fun onGetComics(page: Int) {
        viewModelScope.launch {
            try {
                val comics = getComics(page)
                if (comics.isNotEmpty())
                    mSideEffect.value = ComicsState.ShowComics(comics)
                else
                    mSideEffect.value = ComicsState.ShowEmpty
            } catch (exception: Exception) {
                exception.printStackTrace()
                mSideEffect.value = ComicsState.ShowGenericError(
                    exception.localizedMessage ?: exception.message ?: "Unknown Error"
                )
            }
        }
    }
}