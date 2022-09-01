package com.rappi.marvel.comics.presentation.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.rappi.marvel.R
import com.rappi.marvel.comics.presentation.ComicsEvent
import com.rappi.marvel.comics.presentation.ComicsState
import com.rappi.marvel.comics.presentation.ComicsViewModel
import com.rappi.marvel.databinding.FragmentComicListBinding
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicListFragment : Fragment(R.layout.fragment_comic_list) {
    private val binding: FragmentComicListBinding by viewBindings()
    private val viewModel: ComicsViewModel by viewModels()
    private lateinit var comicAdapter: ComicListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEvent(ComicsEvent.OnGetComics())
        viewModel.sideEffect.observe(viewLifecycleOwner) {
            it?.let { comicState ->
                takeActionOn(comicState)
            }
        }

        comicAdapter = ComicListAdapter(mutableListOf()) {
            // TODO: redirigir.
        }
        binding.root.setOnClickListener {
            Toast.makeText(requireContext(), "test", Toast.LENGTH_SHORT).show()
        }
        binding.apply {
            rvComics.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = comicAdapter
            }
        }
    }

    private fun takeActionOn(comicState: ComicsState) {
        when (comicState) {
            is ComicsState.ShowGenericError -> Toast.makeText(
                requireContext(),
                comicState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
            is ComicsState.ShowComics -> {
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
}