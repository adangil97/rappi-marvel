package com.rappi.marvel.comics.presentation.detail

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.rappi.domain.characters.dto.ModelDtoWrapper
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.marvel.R
import com.rappi.marvel.characters.CharacterAdapter
import com.rappi.marvel.databinding.FragmentComicsDetailBinding
import com.rappi.marvel.utils.load
import com.rappi.marvel.utils.toHexColor
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

/**
 * Contiene la vista del detalle del comic marvel seleccionado.
 */
@AndroidEntryPoint
class ComicsDetailFragment : Fragment(R.layout.fragment_comics_detail) {
    private val binding: FragmentComicsDetailBinding by viewBindings()
    private val args: ComicsDetailFragmentArgs by navArgs()
    private val viewModel: ComicDetailViewModel by viewModels()
    private lateinit var comicCharacterAdapter: CharacterAdapter
    private var snack: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTransition()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        viewModel.onEvent(ComicsDetailEvent.OnGetComicById(args.id))
        viewModel.sideEffect.observe(viewLifecycleOwner) {
            it?.let { comicDetailState ->
                takeActionOn(comicDetailState)
            }
        }
        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadTransition() {
        binding.ivBackground.transitionName = args.urlImage
        binding.ivBackground.load(args.urlImage) {
            it?.let { drawable ->
                viewModel.onEvent(
                    ComicsDetailEvent.OnCalculateDominantColor(drawable)
                )
            }
            startPostponedEnterTransition()
        }
    }

    private fun takeActionOn(state: ComicDetailState) {
        when (state) {
            is ComicDetailState.ShowComic -> loadUi(state.modelDtoWrapper)
            is ComicDetailState.ShowDominantColor -> showDominantColor(state.hexDominantColor)
            is ComicDetailState.ShowErrorMessage -> {
                binding.flLoading.isGone = true
                binding.rvCharacters.isGone = true
                binding.tvTitleCharacters.isGone = true
                binding.tvDescriptionTitle.isGone = true
                snack = Snackbar.make(
                    requireView(),
                    state.errorMessage,
                    Snackbar.LENGTH_INDEFINITE
                )
                snack?.setAction("Reintentar") {
                    snack?.dismiss()
                    binding.flLoading.isGone = false
                    binding.rvCharacters.isGone = false
                    binding.tvTitleCharacters.isGone = false
                    binding.tvDescriptionTitle.isGone = false
                    viewModel.onEvent(ComicsDetailEvent.OnGetComicById(args.id))
                }
                snack?.show()
            }
        }
    }

    private fun showDominantColor(hexDominantColor: String) {
        val colorBackGround = ContextCompat.getColor(
            requireContext(),
            R.color.background
        )
        val hexColorBackGround = colorBackGround.toHexColor()
        val shadowHexColorBackGroundColor = "#FF$hexColorBackGround"
        val shadowHexDominantColor = "#4D$hexDominantColor"
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                shadowHexDominantColor.toColorInt(),
                shadowHexColorBackGroundColor.toColorInt()
            )
        )
        gradient.cornerRadius = 0f
        binding.viewGradient.background = gradient
        requireActivity().window?.statusBarColor = "#$hexDominantColor".toColorInt()
    }

    private fun loadUi(wrapper: ModelDtoWrapper<ComicDto>) {
        binding.flLoading.isGone = true
        binding.tvTitle.text = wrapper.data.title
        binding.tvDescription.text = wrapper.data.description
        if (wrapper.characters.isEmpty()) {
            binding.tvTitleCharacters.isGone = true
            binding.rvCharacters.isGone = true
        } else {
            comicCharacterAdapter = CharacterAdapter(wrapper.characters)
            binding.rvCharacters.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = comicCharacterAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snack?.dismiss()
    }
}