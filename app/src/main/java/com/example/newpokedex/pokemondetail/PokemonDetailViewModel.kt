package com.example.newpokedex.pokemondetail

import androidx.lifecycle.ViewModel
import com.example.newpokedex.data.remote.responses.Pokemon
import com.example.newpokedex.repository.PokemonRepository
import com.example.newpokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }
}