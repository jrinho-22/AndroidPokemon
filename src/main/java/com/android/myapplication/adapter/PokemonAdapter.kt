package com.android.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R
import com.android.myapplication.model.Pokemon
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class PokemonAdapter(private var pokemonList: List<Pokemon>,private var context: Context) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    private val _pokemonList: List<Pokemon> = pokemonList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.nameTextView.text = pokemon.name
        holder.idTextView.text = formateID(pokemon.nameId)

        val colorResId = when (position % 4) {
            0 -> R.color.color1
            1 -> R.color.color2
            2 -> R.color.color3
            else -> R.color.color4
        }
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, colorResId))


         val pokemonId = pokemon.url.split("/".toRegex()).dropLast(1).last()
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imageView)

        holder.cardView.setOnClickListener {
            Toast.makeText(context, pokemon.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun formateID(id: String): CharSequence? {
        return "#${id.padStart(3, '0')}"
    }

    override fun getItemCount() = pokemonList.size

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
    pokemonList = if (query.isEmpty()) {
     _pokemonList
        } else {
           _pokemonList.filter { it.name.contains(query, ignoreCase = true) }
        }


        notifyDataSetChanged()
    }



    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)

        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}








