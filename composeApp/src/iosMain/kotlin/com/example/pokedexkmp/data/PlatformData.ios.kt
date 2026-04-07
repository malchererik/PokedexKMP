package com.example.pokedexkmp.data

actual fun getPlatformExclusivePokemons(): List<Pokemon> {
    return listOf(
        Pokemon(
            id = 144,
            name = "articuno",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/144.png",
            types = listOf("ice", "flying"),
            height = 17, weight = 554,
            stats = listOf(PokemonStat("hp", 90), PokemonStat("attack", 85), PokemonStat("defense", 100), PokemonStat("special-attack", 95), PokemonStat("special-defense", 125), PokemonStat("speed", 85)),
            description = "Um lendário pássaro de gelo. Dizem que aparece para pessoas perdidas em montanhas nevadas."
        ),
        Pokemon(
            id = 145,
            name = "zapdos",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/145.png",
            types = listOf("electric", "flying"),
            height = 16, weight = 526,
            stats = listOf(PokemonStat("hp", 90), PokemonStat("attack", 90), PokemonStat("defense", 85), PokemonStat("special-attack", 125), PokemonStat("special-defense", 90), PokemonStat("speed", 100)),
            description = "Um lendário pássaro elétrico. Tem a habilidade de controlar os raios."
        ),
        Pokemon(
            id = 146,
            name = "moltres",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/146.png",
            types = listOf("fire", "flying"),
            height = 20, weight = 600,
            stats = listOf(PokemonStat("hp", 90), PokemonStat("attack", 100), PokemonStat("defense", 90), PokemonStat("special-attack", 125), PokemonStat("special-defense", 85), PokemonStat("speed", 90)),
            description = "Um lendário pássaro de fogo. Suas asas brilhantes intimidam qualquer adversário."
        ),
        Pokemon(
            id = 249,
            name = "lugia",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/249.png",
            types = listOf("psychic", "flying"),
            height = 52, weight = 2160,
            stats = listOf(PokemonStat("hp", 106), PokemonStat("attack", 90), PokemonStat("defense", 130), PokemonStat("special-attack", 90), PokemonStat("special-defense", 154), PokemonStat("speed", 110)),
            description = "O guardião dos mares. Dorme no fundo do oceano para não causar tempestades."
        )
    )
}