package com.android.myapplication.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myapplication.model.Pokemon
import kotlinx.coroutines.launch


class PokemonViewModel(private val pokemonDao: PokemonDao) : ViewModel() {
    val allPokemonData: LiveData<List<Pokemon>> = pokemonDao.getAllPokes()
     fun insertPokemonData(pokemon: List<Pokemon>) {
        viewModelScope.launch {
            pokemonDao.insertPokemonData(pokemon)

        }
    }
}


