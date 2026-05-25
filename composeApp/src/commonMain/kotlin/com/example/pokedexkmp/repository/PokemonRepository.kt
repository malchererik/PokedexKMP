package com.example.pokedexkmp.repository

import com.example.pokedexkmp.data.PokeApiClient
import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.database.AppDatabase
import com.example.pokedexkmp.database.PokemonCacheEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. A Interface
interface PokemonRepository {
    suspend fun syncPokemonsIfEmpty()
    fun searchPokemonListFlow(query: String = ""): Flow<List<Pokemon>>
    fun getPagedPokemonListFlow(limit: Int, offset: Int): Flow<List<Pokemon>>
    suspend fun getPokemonsPaged(query: String, limit: Int, offset: Int): List<PokemonCacheEntity>
}

// 2. A Implementação
class PokemonRepositoryImpl(
    private val database: AppDatabase
) : PokemonRepository {

    private val apiClient = PokeApiClient()

    override suspend fun syncPokemonsIfEmpty() {
        val count = database.pokemonDao().getCacheCount()
        if (count == 0) {
            try {
                val apiResponse = apiClient.getPokemonList(150)

                val entities = apiResponse.results.mapNotNull { resource ->
                    val id = resource.url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: return@mapNotNull null
                    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

                    PokemonCacheEntity(id = id, name = resource.name, imageUrl = imageUrl)
                }
                database.pokemonDao().insertCache(entities)
            } catch (e: Exception) {
                // Tratamento de erro básico
                println("Erro ao sincronizar Pokémons: ${e.message}")
            }
        }
    }

    override fun searchPokemonListFlow(query: String): Flow<List<Pokemon>> {
        return database.pokemonDao().searchPokemonCache(query).map { entities ->
            entities.map { entity ->
                Pokemon(
                    id = entity.id, name = entity.name, imageUrl = entity.imageUrl,
                    types = emptyList(), height = 0, weight = 0, stats = emptyList(), description = "", captureLocation = ""
                )
            }
        }
    }

    // Executa a paginação no Room
    override fun getPagedPokemonListFlow(limit: Int, offset: Int): Flow<List<Pokemon>> {
        return database.pokemonDao().getPagedCache(limit, offset).map { entities ->
            entities.map { entity ->
                Pokemon(
                    id = entity.id, name = entity.name, imageUrl = entity.imageUrl,
                    types = emptyList(), height = 0, weight = 0, stats = emptyList(), description = "", captureLocation = ""
                )
            }
        }
    }

    override suspend fun getPokemonsPaged(query: String, limit: Int, offset: Int): List<PokemonCacheEntity> {
        return database.pokemonDao().getPokemonsPaged(query, limit, offset)
    }
}
