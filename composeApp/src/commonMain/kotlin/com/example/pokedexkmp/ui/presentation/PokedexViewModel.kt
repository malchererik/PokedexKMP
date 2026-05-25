package com.example.pokedexkmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.repository.PokemonRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



sealed class PokedexUiState {
    object Loading : PokedexUiState()
    data class Success(val pokemons: List<Pokemon>) : PokedexUiState()
    data class Error(val message: String) : PokedexUiState()
}

class PokedexViewModel(private val repository: PokemonRepositoryImpl) : ViewModel() {

    private var currentOffset = 0
    private val limit = 20
    private var currentQuery = ""
    private var isLastPage = false
    private val allPokemons = mutableListOf<Pokemon>()

    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    init {
        loadPokemons()
    }

    fun loadPokemons(query: String = currentQuery, reset: Boolean = false) {
        viewModelScope.launch {
            if (reset) {
                currentOffset = 0
                allPokemons.clear()
                isLastPage = false
                currentQuery = query
                _uiState.value = PokedexUiState.Loading
            }

            if (isLastPage) return@launch

            try {
                repository.syncPokemonsIfEmpty()

                // Busca a página atual direto do Room
                val entities = repository.getPokemonsPaged(currentQuery, limit, currentOffset)

                if (entities.isEmpty()) {
                    isLastPage = true
                } else {
                    val newPokemons = entities.map {
                        Pokemon(it.id, it.name, it.imageUrl, emptyList(), 0, 0, emptyList(), "")
                    }
                    allPokemons.addAll(newPokemons)
                    currentOffset += limit
                }

                _uiState.value = PokedexUiState.Success(allPokemons.toList())
            } catch (e: Exception) {
                _uiState.value = PokedexUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}