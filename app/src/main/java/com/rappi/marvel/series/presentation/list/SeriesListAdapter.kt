package com.rappi.marvel.series.presentation.list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rappi.domain.series.Series
import com.rappi.marvel.R
import com.rappi.marvel.databinding.AdapterSeriesItemBinding
import com.rappi.marvel.series.presentation.list.SeriesListAdapter.SeriesViewHolder

/**
 * @author Adán Castillo.
 */
class SeriesListAdapter(
    val items: MutableList<Series>,
    private val listener: (Series) -> Unit
) : RecyclerView.Adapter<SeriesViewHolder>() {

    inner class SeriesViewHolder(
        private val binding: AdapterSeriesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    listener(item)
                }
            }
        }

        fun bind(item: Series) {
            Glide.with(binding.root.context)
                .load("${item.thumbnail.path}.${item.thumbnail.extension}")
                .placeholder(ColorDrawable(Color.GRAY))
                .into(binding.ivMarvelSerie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder =
        SeriesViewHolder(
            AdapterSeriesItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_series_item, parent, false)
            )
        )

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}