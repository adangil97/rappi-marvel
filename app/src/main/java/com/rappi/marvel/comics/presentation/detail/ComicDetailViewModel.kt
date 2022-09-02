package com.rappi.marvel.comics.presentation.detail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.rappi.marvel.utils.toHexColor
import com.rappi.usecases.comics.GetComicById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Realiza la comunicación con los casos de uso, también informa a UI sobre acciones, estados y efectos.
 *
 * @author Adán Castillo.
 */
@HiltViewModel
class ComicDetailViewModel @Inject constructor(
    private val getComicById: GetComicById
) : ViewModel() {
    private val mSideEffect = MutableLiveData<ComicDetailState?>()
    val sideEffect: LiveData<ComicDetailState?> get() = mSideEffect

    fun onEvent(event: ComicsDetailEvent) {
        when (event) {
            is ComicsDetailEvent.OnCalculateDominantColor -> onCalculateDominantColor(event.drawable)
            is ComicsDetailEvent.OnGetComicById -> onGetComicById(event.id)
        }
    }

    private fun onGetComicById(id: Int) {
        viewModelScope.launch {
            val comic = getComicById(id)
            mSideEffect.value = ComicDetailState.ShowComic(comic)
        }
    }

    private fun onCalculateDominantColor(drawable: Drawable) {
        viewModelScope.launch {
            val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

            Palette.from(bitmap).generate { palette ->
                palette?.dominantSwatch?.rgb?.let { colorValue ->
                    mSideEffect.value = ComicDetailState.ShowDominantColor(colorValue.toHexColor())
                }
            }
        }
    }
}