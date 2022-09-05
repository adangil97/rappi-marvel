package com.rappi.marvel.series.presentation.detail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.rappi.marvel.utils.toHexColor
import com.rappi.usecases.series.GetSerieById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Realiza la comunicación con los casos de uso, también informa a UI sobre acciones, estados y efectos.
 *
 * @author Adán Castillo.
 */
@HiltViewModel
class SeriesDetailViewModel @Inject constructor(
    private val getSerieById: GetSerieById
) : ViewModel() {
    private val mSideEffect = MutableLiveData<SeriesDetailState?>()
    val sideEffect: LiveData<SeriesDetailState?> get() = mSideEffect

    fun onEvent(event: SeriesDetailEvent) {
        when (event) {
            is SeriesDetailEvent.OnCalculateDominantColor -> onCalculateDominantColor(event.drawable)
            is SeriesDetailEvent.OnGetSerieById -> onGetSerieById(event.id)
        }
    }

    private fun onGetSerieById(id: Int) {
        viewModelScope.launch {
            try {
                val serie = getSerieById(id)
                mSideEffect.value = SeriesDetailState.ShowSerie(serie)
            } catch (exception: Exception) {
                exception.printStackTrace()
                mSideEffect.value = SeriesDetailState.ShowErrorMessage(
                    exception.message ?: exception.localizedMessage ?: "Unknown Error"
                )
            }
        }
    }

    private fun onCalculateDominantColor(drawable: Drawable) {
        viewModelScope.launch {
            val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

            Palette.from(bitmap).generate { palette ->
                palette?.dominantSwatch?.rgb?.let { colorValue ->
                    mSideEffect.value = SeriesDetailState.ShowDominantColor(colorValue.toHexColor())
                }
            }
        }
    }
}