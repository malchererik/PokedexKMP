package com.example.pokedexkmp.navigation

import kotlinx.serialization.Serializable

@Serializable
object TeamRoute

@Serializable
object PokedexRoute

@Serializable
data class PokemonDetailRoute(val pokemonId: Int)