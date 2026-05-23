package com.example.pokedexkmp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    // OPERAÇÕES DO TIME (FAVORITOS)
    @Query("SELECT * FROM team_pokemon")
    fun getTeam(): Flow<List<TeamPokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToTeam(pokemon: TeamPokemonEntity)

    @Query("DELETE FROM team_pokemon WHERE id = :id")
    suspend fun removeFromTeam(id: Int)

    // OPERAÇÕES DE CACHE (OFFLINE-FIRST)
    @Query("SELECT * FROM pokemon_cache")
    fun getCache(): Flow<List<PokemonCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(pokemons: List<PokemonCacheEntity>)

    @Query("SELECT COUNT(*) FROM pokemon_cache")
    suspend fun getCacheCount(): Int
}
