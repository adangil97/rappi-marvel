package com.rappi.marvel.comics.presentation.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
class ComicListFragment : Fragment(R.layout.fragment_comic_list) {
    companion object {
        private const val ELEMENTS_TO_SCROLL = 10
    }

    private val binding: FragmentComicListBinding by viewBindings()
    private val viewModel: ComicsViewModel by viewModels()
    private lateinit var comicAdapter: ComicListAdapter
    private var page = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                if (totalItemCount > 0 && endHasBeenReached) {
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
                if (binding.viewShimmer.shimmer.isShimmerVisible)
                    hideInitialLoading()
                comicAdapter.items.addAll(comicState.comics)
                comicAdapter.notifyItemRangeInserted(
                    comicAdapter.items.size,
                    comicState.comics.size
                )
            }
            ComicsState.ShowEmpty -> {
                binding.tvEmpty.isGone = false
            }
        }
    }

    override fun onDestroyView() {
        viewModel.onEvent(ComicsEvent.OnClearSideEffect)
        super.onDestroyView()
    }
}