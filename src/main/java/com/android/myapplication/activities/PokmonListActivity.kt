package com.android.myapplication.activities



import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.lifecycleScope
import com.android.myapplication.db.AppDatabase
import com.android.myapplication.db.PokemonViewModel
import com.android.myapplication.db.PokemonViewModelFactory
import kotlinx.coroutines.launch
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R
import com.android.myapplication.adapter.PokemonAdapter
import com.android.myapplication.apis.RetrofitClient
import com.android.myapplication.model.Pokemon
import com.android.myapplication.model.PokemonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PokmonListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter
    private lateinit var searchEditText: EditText
    private lateinit var pokemonViewModel: PokemonViewModel
    private val pokemonList = mutableListOf<Pokemon>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pokemon)
        searchEditText = findViewById(R.id.search_items)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter(pokemonList,applicationContext)
        recyclerView.adapter = adapter
        val pokeDao = AppDatabase.getDatabase(applicationContext).pokemonDao()
        val viewModelFactory = PokemonViewModelFactory(pokeDao)
        pokemonViewModel = ViewModelProvider(this, viewModelFactory).get(PokemonViewModel::class.java)


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

                adapter.filter(s.toString())
            }
        })

        if (isInternetAvailable()) {
            fetchPokemonData()
        } else {
            loadPokemonsFromDatabase()
        }

        // Observe the LiveData from the ViewModel
        pokemonViewModel.allPokemonData.observe(this) { pokemons ->
            pokemonList.clear()
            pokemonList.addAll(pokemons)
            adapter.notifyDataSetChanged()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun fetchPokemonData() {
        val apiService = RetrofitClient.apiService
        apiService.getPokemonList().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    response.body()?.results?.let { results ->
                        val pokemonEntities = results.map {
                            Pokemon(
                                id = extractPokemonId(it.url),
                                name = it.name,
                                url = it.url
                            )
                        }
                        savePokemonsToDatabase(pokemonEntities)
                    }
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {

            }
        })
    }

    private fun savePokemonsToDatabase(pokemons: List<Pokemon>) {
        lifecycleScope.launch {
            pokemonViewModel.insertPokemonData(pokemons)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPokemonsFromDatabase() {
        lifecycleScope.launch {
          pokemonViewModel.allPokemonData.observe(this@PokmonListActivity){ pokemons ->
                pokemonList.clear()
                pokemonList.addAll(pokemons)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun extractPokemonId(url: String): Int {
        return url.split("/").dropLast(1).last().toInt()
    }
}






