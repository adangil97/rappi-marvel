package com.rappi.marvel.comics.presentation.detail

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
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.marvel.R
import com.rappi.marvel.databinding.FragmentComicsDetailBinding
import com.rappi.marvel.utils.toHexColor
import com.rappi.marvel.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicsDetailFragment : Fragment(R.layout.fragment_comics_detail) {
    private val binding: FragmentComicsDetailBinding by viewBindings()
    private val args: ComicsDetailFragmentArgs by navArgs()
    private val viewModel: ComicDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun takeActionOn(state: ComicDetailState) {
        when (state) {
            is ComicDetailState.ShowComic -> loadUi(state.comic)
            is ComicDetailState.ShowDominantColor -> showDominantColor(state.hexDominantColor)
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

    private fun loadUi(comic: ComicDto) {
        Glide.with(binding.root.context)
            .load(comic.urlImage)
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
                                ComicsDetailEvent.OnCalculateDominantColor(drawable)
                            )
                        }
                        return true
                    }

                }
            )
            .into(binding.ivBackground)
        binding.tvTitle.text = comic.title
        binding.tvDescription.text = comic.description
    }
}