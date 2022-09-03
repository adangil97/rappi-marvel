package com.rappi.marvel.series.presentation.list

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rappi.marvel.R
import com.rappi.marvel.databinding.FragmentSeriesListBinding
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

/**
 * Contiene la vista del listado de series marvel.
 */
@AndroidEntryPoint
class SeriesListFragment : Fragment(R.layout.fragment_series_list), OnQueryTextListener {
    companion object {
        private const val ELEMENTS_TO_SCROLL = 50
        private const val SPAN_COUNT = 3
    }

    private val binding: FragmentSeriesListBinding by viewBindings()
    private val viewModel: SeriesListViewModel by viewModels()
    private lateinit var seriesAdapter: SeriesListAdapter
    private var isSearching = false
    private var isPaging = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.background)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        showMenu()
        showInitialLoading()
        // Obtenemos la primera pagina.
        viewModel.onEvent(SeriesListEvent.OnGetSeries)
        viewModel.sideEffect.observe(viewLifecycleOwner) {
            it?.let { seriesState ->
                takeActionOn(seriesState)
            }
        }

        seriesAdapter = SeriesListAdapter(mutableListOf()) {
            val action =
                SeriesListFragmentDirections.actionSeriesListFragmentToSeriesDetailFragment(
                    it.id
                )
            findNavController().navigate(action)
        }
        binding.rvSeries.apply {
            layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
            adapter = seriesAdapter
        }

        setListeners()
    }

    private fun setListeners() {
        binding.rvSeries.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastVisible = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    val endHasBeenReached = lastVisible >= (totalItemCount - ELEMENTS_TO_SCROLL)
                    if (totalItemCount > 0 && endHasBeenReached && !isSearching) {
                        if (!isPaging) {
                            isPaging = true
                            viewModel.onEvent(SeriesListEvent.OnGetSeries)
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Si el usuario llego al final de la lista.
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val itemCount = layoutManager.itemCount
                    // Obtenemos el ultimo item de la lista
                    val lastItemType = seriesAdapter.items[itemCount - 1]
                    // Si no es de tipo loading.
                    if (lastItemType !is SeriesAdapterItemType.SerieLoadingType) {
                        // Obtenemos cuantos items quedaron en la ultima fila.
                        val lastItemsCount = itemCount % SPAN_COUNT
                        // Obtenemos cuantos items faltan para llenar la fila.
                        val countItems = SPAN_COUNT - lastItemsCount
                        // Llenamos la fila con items de tipo loading para indicar operación en curso.
                        val loadingItemList = IntArray(countItems).map {
                            SeriesAdapterItemType.SerieLoadingType
                        }
                        seriesAdapter.items.addAll(loadingItemList)
                        seriesAdapter.notifyItemRangeInserted(
                            seriesAdapter.items.size,
                            countItems
                        )
                    }
                }
            }
        })
        binding.btnRetry.setOnClickListener {
            binding.btnRetry.isGone = true
            binding.tvError.isGone = true
            showInitialLoading()
            viewModel.onEvent(SeriesListEvent.OnGetSeries)
        }
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

    private fun takeActionOn(seriesState: SeriesListState) {
        when (seriesState) {
            is SeriesListState.ShowGenericError -> {
                val snack = Snackbar.make(
                    requireView(),
                    seriesState.errorMessage,
                    Snackbar.LENGTH_INDEFINITE
                )
                snack.setAction("Ok") {
                    snack.dismiss()
                }
                snack.show()
                hideInitialLoading()
            }
            is SeriesListState.ShowSeries -> {
                val loadingItems =
                    seriesAdapter.items.filterIsInstance<SeriesAdapterItemType.SerieLoadingType>()
                seriesAdapter.items.removeAll(loadingItems)
                seriesAdapter.notifyItemRangeRemoved(
                    seriesAdapter.items.size - loadingItems.size,
                    loadingItems.size
                )
                isPaging = false
                binding.rvSeries.isGone = false
                if (binding.viewShimmer.shimmer.isShimmerVisible)
                    hideInitialLoading()
                seriesAdapter.items.addAll(seriesState.series)
                seriesAdapter.notifyItemRangeInserted(
                    seriesAdapter.items.size,
                    seriesState.series.size
                )
            }
            SeriesListState.ShowEmpty -> {
                hideInitialLoading()
                binding.rvSeries.isGone = true
                binding.tvError.isGone = false
                binding.btnRetry.isGone = false
            }
            is SeriesListState.ShowSearchSeries -> {
                binding.rvSeries.isGone = false
                binding.tvError.isGone = true
                binding.btnRetry.isGone = true
                val size = seriesAdapter.items.size
                seriesAdapter.items.clear()
                seriesAdapter.notifyItemRangeRemoved(0, size)
                seriesAdapter.items.addAll(seriesState.series)
                seriesAdapter.notifyItemRangeInserted(
                    seriesAdapter.items.size,
                    seriesState.series.size
                )
            }
            is SeriesListState.ShowPlaceholderError -> {
                hideInitialLoading()
                binding.rvSeries.isGone = true
                binding.tvError.isGone = false
                binding.btnRetry.isGone = false
                binding.tvError.text = seriesState.errorMessage
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
                binding.tvError.isGone = true
                binding.btnRetry.isGone = true
                isSearching = false
                return true
            }

        })
        val searchView = searchItem.actionView as SearchView
        searchView.inputType = InputType.TYPE_CLASS_TEXT
        searchView.setOnQueryTextListener(this)
    }

    override fun onDestroyView() {
        isPaging = true
        viewModel.onEvent(SeriesListEvent.OnClearSideEffect)
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
            SeriesListEvent.OnSearchSeries(query)
        )
    }
}