package com.rappi.marvel.series.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rappi.data.utils.Resource
import com.rappi.usecases.series.GetAllSeries
import com.rappi.usecases.series.GetSeries
import com.rappi.usecases.series.SearchSeries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
    private val mSideEffect: MutableSharedFlow<SeriesListState> = MutableSharedFlow()
    val sideEffect = mSideEffect.asSharedFlow()

    fun onEvent(event: SeriesListEvent) {
        when (event) {
            is SeriesListEvent.OnSearchSeries -> {
                if (event.query.isNotEmpty()) {
                    onSearchSeries(event.query)
                } else {
                    onGetAllSeries()
                }
            }
            is SeriesListEvent.OnGetSeries -> onGetSeries(event.page)
        }
    }

    private fun onGetSeries(page: Int) {
        viewModelScope.launch {
            getSeries(page)
                .distinctUntilChanged()
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            mSideEffect.emit(SeriesListState.ShowLoading)
                        }
                        is Resource.Success -> {
                            mSideEffect.emit(
                                SeriesListState.ShowSeries(
                                    resource.data.series.map {
                                        SeriesAdapterItemType.SerieDtoType(it)
                                    }
                                )
                            )
                        }
                        is Resource.Error -> {
                            if (resource.data.page == 0) {
                                mSideEffect.emit(
                                    SeriesListState.ShowPlaceholderError(
                                        resource.throwable.localizedMessage
                                            ?: resource.throwable.message
                                            ?: "Unknown Error"
                                    )
                                )
                            } else {
                                mSideEffect.emit(
                                    SeriesListState.ShowGenericError(
                                        resource.throwable.localizedMessage
                                            ?: resource.throwable.message
                                            ?: "Unknown Error"
                                    )
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun onGetAllSeries() {
        viewModelScope.launch {
            val series = getAllSeries()
            if (series.isNotEmpty())
                mSideEffect.emit(
                    SeriesListState.ShowSearchSeries(
                        series.map {
                            SeriesAdapterItemType.SerieDtoType(it)
                        }
                    )
                )
            else
                mSideEffect.emit(
                    SeriesListState.ShowEmpty
                )
        }
    }

    private fun onSearchSeries(query: String) {
        viewModelScope.launch {
            val series = searchSeries(query)
            if (series.isNotEmpty())
                mSideEffect.emit(
                    SeriesListState.ShowSearchSeries(
                        series.map {
                            SeriesAdapterItemType.SerieDtoType(it)
                        }
                    )
                )
            else
                mSideEffect.emit(
                    SeriesListState.ShowEmpty
                )
        }
    }
}