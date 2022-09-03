package com.rappi.marvel.comics.presentation.list

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
import com.rappi.marvel.databinding.FragmentComicListBinding
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

/**
 * Contiene la vista del listado de comics marvel.
 */
@AndroidEntryPoint
class ComicListFragment : Fragment(R.layout.fragment_comic_list), OnQueryTextListener {
    companion object {
        private const val ELEMENTS_TO_SCROLL = 50
        private const val SPAN_COUNT = 3
    }

    private val binding: FragmentComicListBinding by viewBindings()
    private val viewModel: ComicsListViewModel by viewModels()
    private lateinit var comicAdapter: ComicListAdapter
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
        viewModel.onEvent(ComicsListEvent.OnGetComics)
        viewModel.sideEffect.observe(viewLifecycleOwner) {
            it?.let { comicState ->
                takeActionOn(comicState)
            }
        }

        comicAdapter = ComicListAdapter(mutableListOf()) {
            val action = ComicListFragmentDirections.actionComicListFragmentToComicsDetailFragment(
                it.id
            )
            findNavController().navigate(action)
        }
        binding.rvComics.apply {
            layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
            adapter = comicAdapter
        }
        setListeners()
    }

    private fun setListeners() {
        binding.rvComics.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisible = layoutManager.findLastVisibleItemPosition()
                    val endHasBeenReached = lastVisible >= (totalItemCount - ELEMENTS_TO_SCROLL)
                    if (totalItemCount > 0 && endHasBeenReached && !isSearching) {
                        if (!isPaging) {
                            isPaging = true
                            viewModel.onEvent(ComicsListEvent.OnGetComics)
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
                    val lastItemType = comicAdapter.items[itemCount - 1]
                    // Si no es de tipo loading.
                    if (lastItemType !is ComicsAdapterItemType.ComicLoadingType) {
                        // Obtenemos cuantos items quedaron en la ultima fila.
                        val lastItemsCount = itemCount % SPAN_COUNT
                        // Obtenemos cuantos items faltan para llenar la fila.
                        val countItems = SPAN_COUNT - lastItemsCount
                        // Llenamos la fila con items de tipo loading para indicar operación en curso.
                        val loadingItemList = IntArray(countItems).map {
                            ComicsAdapterItemType.ComicLoadingType
                        }
                        comicAdapter.items.addAll(loadingItemList)
                        comicAdapter.notifyItemRangeInserted(
                            comicAdapter.items.size,
                            countItems
                        )
                    }
                }
            }
        })
    }

    private fun showInitialLoading() {
        binding.viewShimmer.shimmer.showShimmer(true)
        binding.viewShimmer.shimmer.isGone = false
        binding.rvComics.isGone = true
    }

    private fun hideInitialLoading() {
        binding.viewShimmer.shimmer.hideShimmer()
        binding.viewShimmer.shimmer.isGone = true
        binding.rvComics.isGone = false
    }

    private fun takeActionOn(comicState: ComicsListState) {
        when (comicState) {
            is ComicsListState.ShowGenericError -> {
                val snack = Snackbar.make(
                    requireView(),
                    comicState.errorMessage,
                    Snackbar.LENGTH_INDEFINITE
                )
                snack.setAction("Ok") {
                    snack.dismiss()
                }
                snack.show()
                hideInitialLoading()
            }
            is ComicsListState.ShowComics -> {
                val loadingItems =
                    comicAdapter.items.filterIsInstance<ComicsAdapterItemType.ComicLoadingType>()
                comicAdapter.items.removeAll(loadingItems)
                comicAdapter.notifyItemRangeRemoved(
                    comicAdapter.items.size - loadingItems.size,
                    loadingItems.size
                )
                isPaging = false
                binding.rvComics.isGone = false
                if (binding.viewShimmer.shimmer.isShimmerVisible)
                    hideInitialLoading()
                comicAdapter.items.addAll(comicState.comics)
                comicAdapter.notifyItemRangeInserted(
                    comicAdapter.items.size,
                    comicState.comics.size
                )
            }
            ComicsListState.ShowEmpty -> {
                hideInitialLoading()
                binding.rvComics.isGone = true
                binding.tvError.isGone = false
            }
            is ComicsListState.ShowSearchComics -> {
                binding.rvComics.isGone = false
                binding.tvError.isGone = true
                val size = comicAdapter.items.size
                comicAdapter.items.clear()
                comicAdapter.notifyItemRangeRemoved(0, size)
                comicAdapter.items.addAll(comicState.comics)
                comicAdapter.notifyItemRangeInserted(
                    comicAdapter.items.size,
                    comicState.comics.size
                )
            }
            is ComicsListState.ShowPlaceholderError -> {
                hideInitialLoading()
                binding.rvComics.isGone = true
                binding.tvError.text = comicState.errorMessage
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
        viewModel.onEvent(ComicsListEvent.OnClearSideEffect)
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
            ComicsListEvent.OnSearchComics(query)
        )
    }
}