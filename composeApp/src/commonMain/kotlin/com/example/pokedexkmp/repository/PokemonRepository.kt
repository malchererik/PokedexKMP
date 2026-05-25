package com.example.pokedexkmp.repository

import com.example.pokedexkmp.data.PokeApiClient
import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.database.AppDatabase
import com.example.pokedexkmp.database.PokemonCacheEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val database: AppDatabase
) {
    private val apiClient = PokeApiClient()

    suspend fun syncPokemonsIfEmpty() {
        val count = database.pokemonDao().getCacheCount()
        if (count == 0) { // Se o banco estiver vazio...
            val apiResponse = apiClient.getPokemonList(150)

            val entities = apiResponse.results.mapNotNull { resource ->
                // Extrai o ID da URL que a PokeAPI devolve
                val id = resource.url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: return@mapNotNull null
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

                PokemonCacheEntity(id = id, name = resource.name, imageUrl = imageUrl)
            }

            database.pokemonDao().insertCache(entities) // !
        }
    }

    //Lê do Banco de Dados em Tempo Real (Flow)
    fun getPokemonListFlow(): Flow<List<Pokemon>> {
        return database.pokemonDao().getCache().map { entities ->
            entities.map { entity ->
                Pokemon(
                    id = entity.id,
                    name = entity.name,
                    imageUrl = entity.imageUrl,
                    types = emptyList(), height = 0, weight = 0, stats = emptyList(), description = ""
                )
            }
        }
    }

    // Busca apenas 1 do banco para a tela de detalhes
    suspend fun getPokemonById(id: Int): Pokemon? {
        return null
    }

    suspend fun getPokemonsPaged(searchQuery: String, limit: Int, offset: Int): List<PokemonCacheEntity> {
        return database.pokemonDao().getPokemonsPaged(searchQuery, limit, offset)
    }
}