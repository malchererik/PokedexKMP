package com.example.pokedexkmp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabela 1: Cache Offline da PokeAPI (Sincronização Inicial M2)
@Entity(tableName = "pokemon_cache")
data class PokemonCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String
)

// Tabela 2: Pokémons do Meu Time (Com a regra de negócio da M2)
@Entity(tableName = "team_pokemon")
data class TeamPokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val captureLocation: String
)