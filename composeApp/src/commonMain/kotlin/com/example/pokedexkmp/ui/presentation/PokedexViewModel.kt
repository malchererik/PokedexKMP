package com.example.pokedexkmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.repository.PokemonRepositoryImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PokedexUiState {
    object Loading : PokedexUiState()
    data class Success(val pokemons: List<Pokemon>, val isLastPage: Boolean = false) : PokedexUiState()
    data class Error(val message: String) : PokedexUiState()
}

class PokedexViewModel(private val repository: PokemonRepositoryImpl) : ViewModel() {

    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    private var currentQuery = ""
    private var currentOffset = 0
    private val pageSize = 20
    private var isLastPage = false
    private val allLoadedPokemons = mutableListOf<Pokemon>()

    private var searchJob: Job? = null

    init {
        loadInitialPokemons("")
    }

    fun onSearchQueryChanged(query: String) {
        loadInitialPokemons(query)
    }

    private fun loadInitialPokemons(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = PokedexUiState.Loading
            currentQuery = query
            currentOffset = 0
            isLastPage = false
            allLoadedPokemons.clear()

            try {
                if (query.isEmpty()) repository.syncPokemonsIfEmpty()
                fetchPage()
            } catch (e: Exception) {
                _uiState.value = PokedexUiState.Error("Erro: ${e.message}")
            }
        }
    }

    // Função chamada quando a tela chega no final do Scroll
    fun loadMorePokemons() {
        if (isLastPage || _uiState.value is PokedexUiState.Loading) return

        searchJob = viewModelScope.launch {
            try {
                fetchPage()
            } catch (e: Exception) {
                _uiState.value = PokedexUiState.Error("Erro: ${e.message}")
            }
        }
    }

    private suspend fun fetchPage() {
        // Busca do Room usando o LIMIT e OFFSET feito pelo seu amigo
        val entities = repository.getPokemonsPaged(currentQuery, pageSize, currentOffset)

        if (entities.isEmpty()) {
            isLastPage = true
        } else {
            val newPokemons = entities.map {
                Pokemon(
                    id = it.id, name = it.name, imageUrl = it.imageUrl,
                    types = emptyList(), height = 0, weight = 0, stats = emptyList(), description = "", captureLocation = ""
                )
            }
            allLoadedPokemons.addAll(newPokemons)
            currentOffset += pageSize
        }

        _uiState.value = PokedexUiState.Success(allLoadedPokemons.toList(), isLastPage)
    }
}