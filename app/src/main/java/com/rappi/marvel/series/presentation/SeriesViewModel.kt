package com.rappi.marvel.series.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rappi.usecases.series.GetAllSeries
import com.rappi.usecases.series.GetSeries
import com.rappi.usecases.series.SearchSeries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Adán Castillo.
 */
@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val getSeries: GetSeries,
    private val searchSeries: SearchSeries,
    private val getAllSeries: GetAllSeries
) : ViewModel() {
    private val mSideEffect = MutableLiveData<SeriesState?>()
    val sideEffect: LiveData<SeriesState?> get() = mSideEffect

    fun onEvent(event: SeriesEvent) {
        when (event) {
            is SeriesEvent.OnGetSeries -> onGetSeries(event.page)
            SeriesEvent.OnClearSideEffect -> mSideEffect.value = null
            is SeriesEvent.OnSearchSeries -> {
                if (event.query.isNotEmpty()) {
                    onSearchSeries(event.query)
                } else {
                    onGetAllSeries()
                }
            }
        }
    }

    private fun onGetAllSeries() {
        viewModelScope.launch {
            val series = getAllSeries()
            if (series.isNotEmpty())
                mSideEffect.value = SeriesState.ShowSearchSeries(series)
            else
                mSideEffect.value = SeriesState.ShowEmpty
        }
    }

    private fun onSearchSeries(query: String) {
        viewModelScope.launch {
            val series = searchSeries(query)
            if (series.isNotEmpty())
                mSideEffect.value = SeriesState.ShowSearchSeries(series)
            else
                mSideEffect.value = SeriesState.ShowEmpty
        }
    }

    private fun onGetSeries(page: Int) {
        viewModelScope.launch {
            try {
                val series = getSeries(page)
                if (series.isEmpty() && page == 0)
                    mSideEffect.value = SeriesState.ShowEmpty
                else
                    mSideEffect.value = SeriesState.ShowSeries(series)
            } catch (exception: Exception) {
                exception.printStackTrace()
                mSideEffect.value = SeriesState.ShowGenericError(
                    exception.localizedMessage ?: exception.message ?: "Unknown Error"
                )
            }
        }
    }
}