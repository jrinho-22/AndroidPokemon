package com.android.myapplication.apis


import com.android.myapplication.model.PokemonResponse
import com.android.myapplication.utils.MyUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface ApiService {
    @GET("pokemon")
    fun getPokemonList(): Call<PokemonResponse>
}

