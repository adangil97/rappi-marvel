package com.rappi.marvel.series.presentation.detail

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
import com.rappi.domain.series.dto.SerieDto
import com.rappi.marvel.R
import com.rappi.marvel.characters.CharacterAdapter
import com.rappi.marvel.databinding.FragmentSeriesDetailBinding
import com.rappi.marvel.utils.load
import com.rappi.marvel.utils.toHexColor
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

/**
 * Contiene la vista del detalle de la serie marvel seleccionada.
 */
@AndroidEntryPoint
class SeriesDetailFragment : Fragment(R.layout.fragment_series_detail) {
    private val binding: FragmentSeriesDetailBinding by viewBindings()
    private val args: SeriesDetailFragmentArgs by navArgs()
    private val viewModel: SeriesDetailViewModel by viewModels()
    private lateinit var seriesCharacterAdapter: CharacterAdapter
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
        viewModel.onEvent(SeriesDetailEvent.OnGetSerieById(args.id))
        viewModel.sideEffect.observe(viewLifecycleOwner) {
            it?.let { seriesDetailState ->
                takeActionOn(seriesDetailState)
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
                    SeriesDetailEvent.OnCalculateDominantColor(drawable)
                )
            }
            startPostponedEnterTransition()
        }
    }

    private fun takeActionOn(state: SeriesDetailState) {
        when (state) {
            is SeriesDetailState.ShowDominantColor -> {
                showDominantColor(state.hexDominantColor)
            }
            is SeriesDetailState.ShowSerie -> loadUi(state.modelDtoWrapper)
            is SeriesDetailState.ShowErrorMessage -> {
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
                    viewModel.onEvent(SeriesDetailEvent.OnGetSerieById(args.id))
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

    private fun loadUi(wrapper: ModelDtoWrapper<SerieDto>) {
        binding.flLoading.isGone = true
        binding.tvTitle.text = wrapper.data.title
        binding.tvDescription.text = wrapper.data.description
        if (wrapper.characters.isEmpty()) {
            binding.tvTitleCharacters.isGone = true
            binding.rvCharacters.isGone = true
        } else {
            seriesCharacterAdapter = CharacterAdapter(wrapper.characters)
            binding.rvCharacters.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = seriesCharacterAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snack?.dismiss()
    }
}