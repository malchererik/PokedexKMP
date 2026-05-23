package com.example.pokedexkmp.repository

import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.database.AppDatabase
import com.example.pokedexkmp.database.PokemonCacheEntity // Importa a entidade
import kotlinx.coroutines.flow.first // Usaremos isso para ler o banco de forma síncrona
import kotlinx.coroutines.runBlocking

interface PokemonRepository {
    fun getPokemonList(): List<Pokemon>
    fun getPokemonById(id: Int): Pokemon?
}

class PokemonRepositoryImpl(
    private val database: AppDatabase
) : PokemonRepository { // <-- O erro sumirá quando você adicionar ": PokemonRepository" aqui

    override fun getPokemonList(): List<Pokemon> {

        return runBlocking {
            database.pokemonDao().getCache().first().map { entity ->
                Pokemon(
                    id = entity.id,
                    name = entity.name,
                    imageUrl = entity.imageUrl,
                    types = emptyList(), // Ajustaremos conforme sua entidade
                    height = 0,
                    weight = 0,
                    stats = emptyList(),
                    description = ""
                )
            }
        }
    }

    override fun getPokemonById(id: Int): Pokemon? {
        // Função específica: Busca apenas um ID no banco
        return getPokemonList().firstOrNull { it.id == id }
    }
}