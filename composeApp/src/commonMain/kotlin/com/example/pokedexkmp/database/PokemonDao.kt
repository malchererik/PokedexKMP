package com.example.pokedexkmp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(pokemons: List<PokemonCacheEntity>)

    @Query("SELECT COUNT(*) FROM pokemon_cache")
    suspend fun getCacheCount(): Int

    // Busca todos (antigo)
    @Query("SELECT * FROM pokemon_cache")
    fun getCache(): Flow<List<PokemonCacheEntity>>

    // NOVO: Paginação exigida pelo professor!
    @Query("SELECT * FROM pokemon_cache LIMIT :limit OFFSET :offset")
    fun getPagedCache(limit: Int, offset: Int): Flow<List<PokemonCacheEntity>>

    @Query("SELECT * FROM pokemon_cache WHERE name LIKE '%' || :searchQuery || '%' LIMIT :limit OFFSET :offset")
    suspend fun getPokemonsPaged(searchQuery: String, limit: Int, offset: Int): List<PokemonCacheEntity>

    // Busca com filtro SQL LIKE
    @Query("SELECT * FROM pokemon_cache WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchPokemonCache(searchQuery: String): Flow<List<PokemonCacheEntity>>

    // --- TIME ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToTeam(pokemon: TeamPokemonEntity)

    @Query("SELECT * FROM team_pokemon")
    fun getTeam(): Flow<List<TeamPokemonEntity>>

    @Query("DELETE FROM team_pokemon WHERE id = :id")
    suspend fun removeFromTeam(id: Int)
}