package com.example.pokedexkmp.repository

import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.data.PokemonMock
import com.example.pokedexkmp.data.getPlatformExclusivePokemons

/**
 * Interface que define o contrato de dados.
 * A UI apenas conhece estas funções, não sabe de onde vêm os dados.
 */
interface PokemonRepository {
    fun getPokemonList(): List<Pokemon>
    fun getPokemonById(id: Int): Pokemon?
}

/**
 * Implementação do repositório que simula o acesso a uma base de dados ou API
 */
class PokemonRepositoryImpl : PokemonRepository {

    override fun getPokemonList(): List<Pokemon> {
        // Junta os Pokémons normais com os exclusivos (vazio no Android, 4 Lendários no iOS)
        return PokemonMock.pokedex + getPlatformExclusivePokemons()
    }

    override fun getPokemonById(id: Int): Pokemon? {
        // Procura o Pokémon dentro da lista completa (incluindo lendários)
        return getPokemonList().firstOrNull { it.id == id }
    }
}