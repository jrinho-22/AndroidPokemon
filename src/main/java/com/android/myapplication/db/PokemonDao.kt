package com.android.myapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.myapplication.model.Pokemon


@Dao
interface PokemonDao {

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertPokemonData(pokemonData: List<Pokemon>)
    @Query("SELECT* FROM pokemon")
    fun getAllPokes(): LiveData<List<Pokemon>>

}
