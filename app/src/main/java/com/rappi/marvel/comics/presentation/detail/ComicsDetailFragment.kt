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
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rappi.domain.characters.dto.ModelDtoWrapper
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.marvel.R
import com.rappi.marvel.characters.CharacterAdapter
import com.rappi.marvel.databinding.FragmentComicsDetailBinding
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
            is ComicDetailState.ShowComic -> loadUi(state.modelDtoWrapper)
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

    private fun loadUi(wrapper: ModelDtoWrapper<ComicDto>) {
        binding.flLoading.isGone = true
        Glide.with(binding.root.context)
            .load(wrapper.data.urlImage)
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
}