package com.rappi.marvel.series.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rappi.data.utils.Resource
import com.rappi.usecases.series.GetAllSeries
import com.rappi.usecases.series.GetSeries
import com.rappi.usecases.series.SearchSeries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Realiza la comunicación con los casos de uso, también informa a UI sobre acciones, estados y efectos.
 *
 * @author Adán Castillo.
 */
@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class SeriesListViewModel @Inject constructor(
    private val getSeries: GetSeries,
    private val searchSeries: SearchSeries,
    private val getAllSeries: GetAllSeries
) : ViewModel() {
    private val mSideEffect = MutableLiveData<SeriesListState?>()
    val sideEffect: LiveData<SeriesListState?> get() = mSideEffect

    val pagingEvent: (SeriesListFilter) -> Unit
    val pagingDataFlow: Flow<SeriesListState>

    init {
        val actionStateFlow = MutableSharedFlow<SeriesListFilter>()
        val filteredScrolled = actionStateFlow
            .distinctUntilChanged { old, new ->
                old.page == new.page || new.requireMoreData
            }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )

        pagingDataFlow = filteredScrolled
            .flatMapLatest { filter ->
                getSeries(filter.page)
            }.map { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        SeriesListState.ShowLoading
                    }
                    is Resource.Success -> {
                        SeriesListState.ShowSeries(
                            resource.data.series.map {
                                SeriesAdapterItemType.SerieDtoType(it)
                            }
                        )
                    }
                    is Resource.Error -> {
                        if (resource.data.page == 0) {
                            SeriesListState.ShowPlaceholderError(
                                resource.throwable.localizedMessage ?: resource.throwable.message
                                ?: "Unknown Error"
                            )
                        } else {
                            SeriesListState.ShowGenericError(
                                resource.throwable.localizedMessage ?: resource.throwable.message
                                ?: "Unknown Error"
                            )
                        }
                    }
                }
            }

        pagingEvent = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    fun onEvent(event: SeriesListEvent) {
        when (event) {
            SeriesListEvent.OnClearSideEffect -> {
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
}