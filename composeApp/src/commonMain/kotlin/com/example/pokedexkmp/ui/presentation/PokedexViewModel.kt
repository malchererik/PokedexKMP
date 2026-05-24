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

    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState.asStateFlow()

    init {
        loadPokemons()
    }

    private fun loadPokemons() {
        viewModelScope.launch {
            _uiState.value = PokedexUiState.Loading
            try {
                // 1. Verifica se precisa de baixar da internet
                repository.syncPokemonsIfEmpty()

                // 2. Fica a ouvir o banco de dados e atualiza a tela automaticamente!
                repository.getPokemonListFlow().collect { pokemons ->
                    _uiState.value = PokedexUiState.Success(pokemons)
                }
            } catch (e: Exception) {
                _uiState.value = PokedexUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }
}