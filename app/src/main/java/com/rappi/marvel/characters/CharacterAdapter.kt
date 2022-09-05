package com.rappi.marvel.characters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rappi.domain.characters.dto.CharacterDto
import com.rappi.marvel.R
import com.rappi.marvel.databinding.AdapterItemCharacterBinding
import com.rappi.marvel.characters.CharacterAdapter.CharacterViewHolder

/**
 * @author Adán Castillo.
 */
class CharacterAdapter(
    private var items: List<CharacterDto>
) : RecyclerView.Adapter<CharacterViewHolder>() {

    inner class CharacterViewHolder(
        private val binding: AdapterItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterDto) {
            Glide.with(binding.root.context)
                .load(item.urlImage)
                .placeholder(ColorDrawable(Color.GRAY))
                .into(binding.ivCharacter)
            binding.tvCharacterTitle.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder =
        CharacterViewHolder(
            AdapterItemCharacterBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_item_character, parent, false)
            )
        )

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}