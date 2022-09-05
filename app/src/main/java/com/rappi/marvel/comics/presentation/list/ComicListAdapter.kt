package com.rappi.marvel.comics.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.marvel.R
import com.rappi.marvel.databinding.AdapterComicsItemBinding
import com.rappi.marvel.databinding.AdapterItemLoadingShimmerBinding
import com.rappi.marvel.utils.load

/**
 * Contiene la vista de item comic de marvel.
 *
 * @param items [MutableList] es el listado de comics marvel.
 * @param listener [Unit] permite informar sobre que elemento se dio click.
 *
 * @author Adán Castillo.
 */
class ComicListAdapter(
    val items: MutableList<ComicsAdapterItemType>,
    private val listener: (ComicDto, ImageView) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    inner class ComicsLoadingViewHolder(
        binding: AdapterItemLoadingShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class ComicsViewHolder(
        private val binding: AdapterComicsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    if (item is ComicsAdapterItemType.ComicDtoType)
                        listener(item.comicDto, binding.ivMarvelComic)
                }
            }
        }

        fun bind(item: ComicDto) {
            binding.ivMarvelComic.apply {
                load(item.urlImage)
                transitionName = item.urlImage
            }
            binding.tvTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_ITEM) {
            ComicsViewHolder(
                AdapterComicsItemBinding.bind(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.adapter_comics_item, parent, false)
                )
            )
        } else {
            ComicsLoadingViewHolder(
                AdapterItemLoadingShimmerBinding.bind(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.adapter_item_loading_shimmer, parent, false)
                )
            )
        }

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is ComicsAdapterItemType.ComicDtoType -> TYPE_ITEM
            ComicsAdapterItemType.ComicLoadingType -> TYPE_LOADING
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (item is ComicsAdapterItemType.ComicDtoType)
            (holder as ComicsViewHolder).bind(item.comicDto)
    }

    override fun getItemCount(): Int = items.size
}