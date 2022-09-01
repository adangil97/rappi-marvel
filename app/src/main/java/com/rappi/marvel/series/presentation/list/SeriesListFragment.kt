package com.rappi.marvel.series.presentation.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rappi.marvel.R
import com.rappi.marvel.databinding.FragmentSeriesListBinding
import com.rappi.marvel.series.presentation.SeriesEvent
import com.rappi.marvel.series.presentation.SeriesState
import com.rappi.marvel.series.presentation.SeriesViewModel
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SeriesListFragment : Fragment(R.layout.fragment_series_list) {
    companion object {
        private const val ELEMENTS_TO_SCROLL = 10
    }

    private val binding: FragmentSeriesListBinding by viewBindings()
    private val viewModel: SeriesViewModel by viewModels()
    private lateinit var seriesAdapter: SeriesListAdapter
    private var page = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showInitialLoading()
        viewModel.onEvent(SeriesEvent.OnGetSeries(page))
        viewModel.sideEffect.observe(viewLifecycleOwner) {
            it?.let { seriesState ->
                takeActionOn(seriesState)
            }
        }

        seriesAdapter = SeriesListAdapter(mutableListOf()) {
            // TODO: redirigir.
        }
        binding.rvSeries.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = seriesAdapter
        }

        setListeners()
    }

    private fun setListeners() {
        binding.rvSeries.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible >= totalItemCount - ELEMENTS_TO_SCROLL
                if (totalItemCount > 0 && endHasBeenReached) {
                    viewModel.onEvent(SeriesEvent.OnGetSeries(++page))
                }
            }
        })
    }

    private fun showInitialLoading() {
        binding.viewShimmer.shimmer.showShimmer(true)
        binding.viewShimmer.shimmer.isGone = false
        binding.rvSeries.isGone = true
    }

    private fun hideInitialLoading() {
        binding.viewShimmer.shimmer.hideShimmer()
        binding.viewShimmer.shimmer.isGone = true
        binding.rvSeries.isGone = false
    }

    private fun takeActionOn(seriesState: SeriesState) {
        when (seriesState) {
            is SeriesState.ShowGenericError -> Toast.makeText(
                requireContext(),
                seriesState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
            is SeriesState.ShowSeries -> {
                if (binding.viewShimmer.shimmer.isShimmerVisible)
                    hideInitialLoading()
                seriesAdapter.items.addAll(seriesState.series)
                seriesAdapter.notifyItemRangeInserted(
                    seriesAdapter.items.size,
                    seriesState.series.size
                )
            }
            SeriesState.ShowEmpty -> {
                binding.tvEmpty.isGone = false
            }
        }
    }

    override fun onDestroyView() {
        viewModel.onEvent(SeriesEvent.OnClearSideEffect)
        super.onDestroyView()
    }
}