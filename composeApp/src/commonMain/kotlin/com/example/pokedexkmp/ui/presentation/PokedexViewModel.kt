package com.example.pokedexkmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.repository.PokemonRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PokedexUiState {
    object Loading : PokedexUiState()
    data class Success(val pokemons: List<Pokemon>) : PokedexUiState()
    data class Error(val message: String) : PokedexUiState()
}

class PokedexViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPokemons("")
    }

    fun onSearchQueryChanged(query: String) {
        loadPokemons(query)
    }

    private fun loadPokemons(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                if (query.isEmpty()) {
                    _uiState.value = PokedexUiState.Loading
                    repository.syncPokemonsIfEmpty()
                }

                repository.searchPokemonListFlow(query).collect { pokemons ->
                    _uiState.value = PokedexUiState.Success(pokemons)
                }
            } catch (e: Exception) {
                _uiState.value = PokedexUiState.Error("Erro no banco: ${e.message}")
            }
        }
    }
}