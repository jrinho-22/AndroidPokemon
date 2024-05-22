package com.android.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pokemon")
data class Pokemon(

@PrimaryKey(autoGenerate = true)
val id:Int,
    val name: String,
    val url: String

)
{
    val nameId: String
        get() = url.split("/".toRegex()).dropLast(1).last()
}

data class PokemonResponse(
    val results: List<Pokemon>
)
