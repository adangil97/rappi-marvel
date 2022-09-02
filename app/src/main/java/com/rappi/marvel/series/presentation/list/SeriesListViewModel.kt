package com.rappi.marvel.series.presentation.list

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
class SeriesListViewModel @Inject constructor(
    private val getSeries: GetSeries,
    private val searchSeries: SearchSeries,
    private val getAllSeries: GetAllSeries
) : ViewModel() {
    private val mSideEffect = MutableLiveData<SeriesListState?>()
    val sideEffect: LiveData<SeriesListState?> get() = mSideEffect

    fun onEvent(event: SeriesListEvent) {
        when (event) {
            is SeriesListEvent.OnGetSeries -> onGetSeries(event.page)
            SeriesListEvent.OnClearSideEffect -> mSideEffect.value = null
            is SeriesListEvent.OnSearchSeries -> {
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
                mSideEffect.value = SeriesListState.ShowSearchSeries(series)
            else
                mSideEffect.value = SeriesListState.ShowEmpty
        }
    }

    private fun onSearchSeries(query: String) {
        viewModelScope.launch {
            val series = searchSeries(query)
            if (series.isNotEmpty())
                mSideEffect.value = SeriesListState.ShowSearchSeries(series)
            else
                mSideEffect.value = SeriesListState.ShowEmpty
        }
    }

    private fun onGetSeries(page: Int) {
        viewModelScope.launch {
            try {
                val series = getSeries(page)
                if (series.isEmpty() && page == 0)
                    mSideEffect.value = SeriesListState.ShowEmpty
                else
                    mSideEffect.value = SeriesListState.ShowSeries(series)
            } catch (exception: Exception) {
                exception.printStackTrace()
                mSideEffect.value = SeriesListState.ShowGenericError(
                    exception.localizedMessage ?: exception.message ?: "Unknown Error"
                )
            }
        }
    }
}