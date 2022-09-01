package com.rappi.marvel.series.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rappi.usecases.series.GetSeries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Adán Castillo.
 */
@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val getSeries: GetSeries
) : ViewModel() {
    private val mSideEffect = MutableLiveData<SeriesState>()
    val sideEffect: LiveData<SeriesState> get() = mSideEffect

    fun onEvent(event: SeriesEvent) {
        when (event) {
            is SeriesEvent.OnGetSeries -> onGetSeries(event.page)
        }
    }

    private fun onGetSeries(page: Int) {
        viewModelScope.launch {
            try {
                val series = getSeries(page)
                if (series.isNotEmpty())
                    mSideEffect.value = SeriesState.ShowSeries(series)
                else
                    mSideEffect.value = SeriesState.ShowEmpty
            } catch (exception: Exception) {
                exception.printStackTrace()
                mSideEffect.value = SeriesState.ShowGenericError(
                    exception.localizedMessage ?: exception.message ?: "Unknown Error"
                )
            }
        }
    }
}