/*package com.example.appcomposeretrofit.screens;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcomposeretrofit.model.Pokemon
import com.example.appcomposeretrofit.repository.PokemonRepository
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
public class PokemonViewModel @Inject constructor(
    private val repo : PokemonRepository
): ViewModel(){
    private val _state = MutableStateFlow(emptyList<Pokemon>())
    val state :StateFlow<List<Pokemon>>
        get() = _state

    init{
        viewModelScope.launch {
            _state.value = repo.getPokemonList().results
        }
    }
}

*/

package com.example.appcomposeretrofit.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcomposeretrofit.model.Pokemon
import com.example.appcomposeretrofit.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val repository: PokemonRepository
) : ViewModel() {
    // Estado que expone la lista de Pokémon
    private val _state = MutableStateFlow<List<Pokemon>>(emptyList())
    val state: StateFlow<List<Pokemon>> = _state

    // Inicializa el ViewModel y carga los datos
    init {
        fetchPokemonList()
    }

    // Función para cargar la lista de Pokémon desde el repository
    private fun fetchPokemonList() {
        viewModelScope.launch {
            try {
                val pokemonList = repository.getPokemonList().results
                _state.value = pokemonList
            } catch (e: Exception) {
                // Manejar errores de red o de datos
                e.printStackTrace()
            }
        }
    }
}
