package com.rappi.marvel.comics.presentation.list

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rappi.domain.comics.dto.ComicDto
import com.rappi.marvel.R
import com.rappi.marvel.comics.presentation.list.ComicListAdapter.ComicsViewHolder
import com.rappi.marvel.databinding.AdapterComicsItemBinding

/**
 * @author Adán Castillo.
 */
class ComicListAdapter(
    val items: MutableList<ComicDto>,
    private val listener: (ComicDto) -> Unit
) : RecyclerView.Adapter<ComicsViewHolder>() {

    inner class ComicsViewHolder(
        private val binding: AdapterComicsItemBinding
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

        fun bind(item: ComicDto) {
            Glide.with(binding.root.context)
                .load(item.urlImage)
                .placeholder(ColorDrawable(Color.GRAY))
                .into(binding.ivMarvelComic)
            binding.tvTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder =
        ComicsViewHolder(
            AdapterComicsItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_comics_item, parent, false)
            )
        )

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}