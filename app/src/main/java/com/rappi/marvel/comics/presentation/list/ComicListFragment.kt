package com.rappi.marvel.comics.presentation.list

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
import com.rappi.marvel.comics.presentation.ComicsEvent
import com.rappi.marvel.comics.presentation.ComicsState
import com.rappi.marvel.comics.presentation.ComicsViewModel
import com.rappi.marvel.databinding.FragmentComicListBinding
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicListFragment : Fragment(R.layout.fragment_comic_list), OnQueryTextListener {
    companion object {
        private const val ELEMENTS_TO_SCROLL = 10
    }

    private val binding: FragmentComicListBinding by viewBindings()
    private val viewModel: ComicsViewModel by viewModels()
    private lateinit var comicAdapter: ComicListAdapter
    private var page = 0
    private var isSearching = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMenu()
        showInitialLoading()
        viewModel.onEvent(ComicsEvent.OnGetComics(page))
        viewModel.sideEffect.observe(viewLifecycleOwner) {
            it?.let { comicState ->
                takeActionOn(comicState)
            }
        }

        comicAdapter = ComicListAdapter(mutableListOf()) {
            // TODO: redirigir.
        }
        binding.rvComics.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = comicAdapter
        }
        setListeners()
    }

    private fun setListeners() {
        binding.rvComics.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible >= totalItemCount - ELEMENTS_TO_SCROLL
                if (totalItemCount > 0 && endHasBeenReached && !isSearching) {
                    viewModel.onEvent(ComicsEvent.OnGetComics(++page))
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

    private fun takeActionOn(comicState: ComicsState) {
        when (comicState) {
            is ComicsState.ShowGenericError -> Toast.makeText(
                requireContext(),
                comicState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
            is ComicsState.ShowComics -> {
                binding.rvComics.isGone = false
                if (binding.viewShimmer.shimmer.isShimmerVisible)
                    hideInitialLoading()
                comicAdapter.items.addAll(comicState.comics)
                comicAdapter.notifyItemRangeInserted(
                    comicAdapter.items.size,
                    comicState.comics.size
                )
            }
            ComicsState.ShowEmpty -> {
                binding.rvComics.isGone = true
                binding.tvEmpty.isGone = false
            }
            is ComicsState.ShowSearchComics -> {
                binding.rvComics.isGone = false
                comicAdapter.items.clear()
                comicAdapter.notifyItemRangeRemoved(0, comicAdapter.items.size)
                comicAdapter.items.addAll(comicState.comics)
                comicAdapter.notifyItemRangeInserted(
                    comicAdapter.items.size,
                    comicState.comics.size
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
        viewModel.onEvent(ComicsEvent.OnClearSideEffect)
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
            ComicsEvent.OnSearchComics(query)
        )
    }
}