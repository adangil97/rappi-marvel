package com.rappi.marvel.series.presentation.list

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
class SeriesListFragment : Fragment(R.layout.fragment_series_list), OnQueryTextListener {
    companion object {
        private const val ELEMENTS_TO_SCROLL = 10
    }

    private val binding: FragmentSeriesListBinding by viewBindings()
    private val viewModel: SeriesViewModel by viewModels()
    private lateinit var seriesAdapter: SeriesListAdapter
    private var page = 0
    private var isSearching = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMenu()
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
                if (totalItemCount > 0 && endHasBeenReached && !isSearching) {
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
                binding.rvSeries.isGone = false
                if (binding.viewShimmer.shimmer.isShimmerVisible)
                    hideInitialLoading()
                seriesAdapter.items.addAll(seriesState.series)
                seriesAdapter.notifyItemRangeInserted(
                    seriesAdapter.items.size,
                    seriesState.series.size
                )
            }
            SeriesState.ShowEmpty -> {
                binding.rvSeries.isGone = true
                binding.tvEmpty.isGone = false
            }
            is SeriesState.ShowSearchSeries -> {
                binding.rvSeries.isGone = false
                val size = seriesAdapter.items.size
                seriesAdapter.items.clear()
                seriesAdapter.notifyItemRangeRemoved(0, size)
                seriesAdapter.items.addAll(seriesState.series)
                seriesAdapter.notifyItemRangeInserted(
                    seriesAdapter.items.size,
                    seriesState.series.size
                )
            }
        }
    }

    private fun showMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)
                createMenu(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun createMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.item_search)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                isSearching = true
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding.tvEmpty.isGone = true
                isSearching = false
                return true
            }

        })
        val searchView = searchItem.actionView as SearchView
        searchView.inputType = InputType.TYPE_CLASS_TEXT
        searchView.setOnQueryTextListener(this)
    }

    override fun onDestroyView() {
        viewModel.onEvent(SeriesEvent.OnClearSideEffect)
        super.onDestroyView()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        search(query)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        search(newText)
        return true
    }

    private fun search(query: String) {
        viewModel.onEvent(
            SeriesEvent.OnSearchSeries(query)
        )
    }
}