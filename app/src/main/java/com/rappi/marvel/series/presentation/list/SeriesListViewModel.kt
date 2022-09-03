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
 * Realiza la comunicación con los casos de uso, también informa a UI sobre acciones, estados y efectos.
 *
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

    private var page = 0

    fun onEvent(event: SeriesListEvent) {
        when (event) {
            SeriesListEvent.OnGetSeries -> onGetSeries()
            SeriesListEvent.OnClearSideEffect -> {
                page = 0
                mSideEffect.value = null
            }
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
                mSideEffect.value = SeriesListState.ShowSearchSeries(
                    series.map {
                        SeriesAdapterItemType.SerieDtoType(it)
                    }
                )
            else
                mSideEffect.value = SeriesListState.ShowEmpty
        }
    }

    private fun onSearchSeries(query: String) {
        viewModelScope.launch {
            val series = searchSeries(query)
            if (series.isNotEmpty())
                mSideEffect.value = SeriesListState.ShowSearchSeries(
                    series.map {
                        SeriesAdapterItemType.SerieDtoType(it)
                    }
                )
            else
                mSideEffect.value = SeriesListState.ShowEmpty
        }
    }

    private fun onGetSeries() {
        viewModelScope.launch {
            try {
                val series = getSeries(page)
                if (series.isEmpty() && page == 0)
                    mSideEffect.value = SeriesListState.ShowEmpty
                else {
                    page += 1
                    mSideEffect.value = SeriesListState.ShowSeries(
                        series.map {
                            SeriesAdapterItemType.SerieDtoType(it)
                        }
                    )
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                if (page == 0)
                    mSideEffect.value = SeriesListState.ShowPlaceholderError(
                        exception.localizedMessage ?: exception.message ?: "Unknown Error"
                    )
                else
                    mSideEffect.value = SeriesListState.ShowGenericError(
                        exception.localizedMessage ?: exception.message ?: "Unknown Error"
                    )
            }
        }
    }
}