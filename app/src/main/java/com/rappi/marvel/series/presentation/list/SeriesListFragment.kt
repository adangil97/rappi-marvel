package com.rappi.marvel.series.presentation.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.rappi.marvel.R
import com.rappi.marvel.databinding.FragmentSeriesListBinding
import com.rappi.marvel.series.presentation.SeriesState
import com.rappi.marvel.series.presentation.SeriesViewModel
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeriesListFragment : Fragment(R.layout.fragment_series_list) {
    private val binding: FragmentSeriesListBinding by viewBindings()
    private val viewModel: SeriesViewModel by viewModels()
    private lateinit var seriesAdapter: SeriesListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sideEffect.observe(viewLifecycleOwner) {
            it?.let { seriesState ->
                takeActionon(seriesState)
            }
        }

        seriesAdapter = SeriesListAdapter(mutableListOf()) {
            // TODO: redirigir.
        }
        binding.apply {
            rvSeries.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = seriesAdapter
            }
        }
    }

    private fun takeActionon(seriesState: SeriesState) {
        when (seriesState) {
            is SeriesState.ShowGenericError -> Toast.makeText(
                requireContext(),
                seriesState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
            is SeriesState.ShowSeries -> {
                seriesAdapter.items.addAll(seriesState.series)
                seriesAdapter.notifyItemRangeInserted(
                    seriesAdapter.items.size,
                    seriesState.series.size
                )
            }
        }
    }
}