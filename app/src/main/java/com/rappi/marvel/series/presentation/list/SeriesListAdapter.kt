package com.rappi.marvel.series.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.rappi.domain.series.dto.SerieDto
import com.rappi.marvel.R
import com.rappi.marvel.databinding.AdapterItemLoadingShimmerBinding
import com.rappi.marvel.databinding.AdapterSeriesItemBinding
import com.rappi.marvel.utils.load

/**
 * Contiene la vista de item serie de marvel.
 *
 * @param items [MutableList] es el listado de series marvel.
 * @param listener [Unit] permite informar sobre que elemento se dio click.
 *
 * @author Adán Castillo.
 */
class SeriesListAdapter(
    val items: MutableList<SeriesAdapterItemType>,
    private val listener: (SerieDto, ImageView) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    inner class SeriesLoadingViewHolder(
        binding: AdapterItemLoadingShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    inner class SeriesViewHolder(
        private val binding: AdapterSeriesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    if (item is SeriesAdapterItemType.SerieDtoType)
                        listener(item.serieDto, binding.ivMarvelSerie)
                }
            }
        }

        fun bind(item: SerieDto) {
            binding.ivMarvelSerie.apply {
                load(item.urlImage)
                transitionName = item.urlImage
            }
            binding.tvTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_ITEM) {
            SeriesViewHolder(
                AdapterSeriesItemBinding.bind(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.adapter_series_item, parent, false)
                )
            )
        } else {
            SeriesLoadingViewHolder(
                AdapterItemLoadingShimmerBinding.bind(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.adapter_item_loading_shimmer, parent, false)
                )
            )
        }

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is SeriesAdapterItemType.SerieDtoType -> TYPE_ITEM
            SeriesAdapterItemType.SerieLoadingType -> TYPE_LOADING
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (item is SeriesAdapterItemType.SerieDtoType)
            (holder as SeriesViewHolder).bind(item.serieDto)
    }

    override fun getItemCount(): Int = items.size
}