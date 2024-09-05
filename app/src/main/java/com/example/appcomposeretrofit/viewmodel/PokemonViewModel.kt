
package com.example.appcomposeretrofit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcomposeretrofit.model.Pokemon
import com.example.appcomposeretrofit.model.PokemonDetail
import com.example.appcomposeretrofit.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow<PokemonDetail?>(null)
    val pokemonDetail: StateFlow<PokemonDetail?> get() = _pokemonDetail

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> get() = _pokemonList

    fun getPokemonDetail(id: Int) {
        viewModelScope.launch {
            try {
                val detail = repository.getPokemonDetail(id)
                _pokemonDetail.value = detail
            } catch (e: Exception) {
                e.printStackTrace() // Manejar el error
            }
        }
    }

    fun getPokemonList(offset: Int = 0, limit: Int = 25) {
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(offset, limit)
                _pokemonList.value = response.results
            } catch (e: Exception) {
                e.printStackTrace() // Manejar el error
            }
        }
    }
}
