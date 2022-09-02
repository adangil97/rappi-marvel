package com.rappi.marvel.series.presentation.detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rappi.domain.series.dto.SerieDto
import com.rappi.marvel.R
import com.rappi.marvel.databinding.FragmentSeriesDetailBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun takeActionOn(state: SeriesDetailState) {
        when (state) {
            is SeriesDetailState.ShowDominantColor -> {
                showDominantColor(state.hexDominantColor)
            }
            is SeriesDetailState.ShowSerie -> loadUi(state.serie)
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

    private fun loadUi(serie: SerieDto) {
        Glide.with(binding.root.context)
            .load(serie.urlImage)
            .placeholder(ColorDrawable(Color.GRAY))
            .addListener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let { drawable ->
                            binding.ivBackground.setImageDrawable(drawable)
                            viewModel.onEvent(
                                SeriesDetailEvent.OnCalculateDominantColor(drawable)
                            )
                        }
                        return true
                    }

                }
            )
            .into(binding.ivBackground)
        binding.tvTitle.text = serie.title
        binding.tvDescription.text = serie.description
    }
}