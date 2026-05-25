package com.example.pokedexkmp.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Cliente Ktor com serialização JSON
val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true }) // Ignora os campos que não precisamos
    }
}

class PokeApiClient {
    suspend fun getPokemonDetails(id: Int): PokemonDto {
        return httpClient.get("https://pokeapi.co/api/v2/pokemon/$id").body()
    }

    // NOVO: Busca a lista inicial de 150 Pokémons
    suspend fun getPokemonList(limit: Int = 150, offset: Int = 0): PokemonListResponseDto {
        return httpClient.get("https://pokeapi.co/api/v2/pokemon?limit=$limit&offset=$offset").body()
    }
}

// ==========================================
// DTOs (Data Transfer Objects)
// ==========================================

// NOVO: DTO para a resposta da lista
@Serializable
data class PokemonListResponseDto(val results: List<NamedApiResourceDto>)

// Os outros DTOs continuam iguais
@Serializable
data class PokemonDto(val id: Int, val name: String, val height: Int, val weight: Int, val sprites: SpritesDto, val types: List<TypeSlotDto>, val stats: List<StatSlotDto>)

@Serializable
data class SpritesDto(val other: OtherSpritesDto? = null)

@Serializable
data class OtherSpritesDto(@SerialName("official-artwork") val officialArtwork: OfficialArtworkDto? = null)

@Serializable
data class OfficialArtworkDto(@SerialName("front_default") val frontDefault: String? = null)

@Serializable
data class TypeSlotDto(val type: NamedApiResourceDto)

@Serializable
data class StatSlotDto(@SerialName("base_stat") val baseStat: Int, val stat: NamedApiResourceDto)

// ADICIONADO A 'url' AQUI:
@Serializable
data class NamedApiResourceDto(val name: String, val url: String = "")