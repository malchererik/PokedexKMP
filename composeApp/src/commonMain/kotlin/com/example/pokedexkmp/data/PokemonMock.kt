package com.example.pokedexkmp.data

object PokemonMock {

    val pokedex = listOf(
        Pokemon(
            id = 1,
            name = "bulbasaur",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            types = listOf("grass", "poison"),
            height = 7,
            weight = 69,
            stats = listOf(
                PokemonStat("hp", 45),
                PokemonStat("attack", 49),
                PokemonStat("defense", 49),
                PokemonStat("special-attack", 65),
                PokemonStat("special-defense", 65),
                PokemonStat("speed", 45)
            ),
            description = "Bulbasaur carrega uma semente em suas costas desde o nascimento."
        ),
        Pokemon(
            id = 4,
            name = "charmander",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png",
            types = listOf("fire"),
            height = 6,
            weight = 85,
            stats = listOf(
                PokemonStat("hp", 39),
                PokemonStat("attack", 52),
                PokemonStat("defense", 43),
                PokemonStat("special-attack", 60),
                PokemonStat("special-defense", 50),
                PokemonStat("speed", 65)
            ),
            description = "Charmander possui uma chama na ponta da cauda que indica sua vitalidade."
        ),
        Pokemon(
            id = 7,
            name = "squirtle",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png",
            types = listOf("water"),
            height = 5,
            weight = 90,
            stats = listOf(
                PokemonStat("hp", 44),
                PokemonStat("attack", 48),
                PokemonStat("defense", 65),
                PokemonStat("special-attack", 50),
                PokemonStat("special-defense", 64),
                PokemonStat("speed", 43)
            ),
            description = "Squirtle se protege com seu casco e lança jatos d’água."
        ),
        Pokemon(
            id = 25,
            name = "pikachu",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
            types = listOf("electric"),
            height = 4,
            weight = 60,
            stats = listOf(
                PokemonStat("hp", 35),
                PokemonStat("attack", 55),
                PokemonStat("defense", 40),
                PokemonStat("special-attack", 50),
                PokemonStat("special-defense", 50),
                PokemonStat("speed", 90)
            ),
            description = "Pikachu armazena eletricidade em suas bochechas."
        ),
        Pokemon(
            id = 39,
            name = "jigglypuff",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/39.png",
            types = listOf("normal", "fairy"),
            height = 5,
            weight = 55,
            stats = listOf(
                PokemonStat("hp", 115),
                PokemonStat("attack", 45),
                PokemonStat("defense", 20),
                PokemonStat("special-attack", 45),
                PokemonStat("special-defense", 25),
                PokemonStat("speed", 20)
            ),
            description = "Jigglypuff encanta adversários com sua canção."
        ),
        Pokemon(
            id = 133,
            name = "eevee",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/133.png",
            types = listOf("normal"),
            height = 3,
            weight = 65,
            stats = listOf(
                PokemonStat("hp", 55),
                PokemonStat("attack", 55),
                PokemonStat("defense", 50),
                PokemonStat("special-attack", 45),
                PokemonStat("special-defense", 65),
                PokemonStat("speed", 55)
            ),
            description = "Eevee possui uma estrutura genética instável e várias evoluções possíveis."
        ),

        Pokemon(
            id = 16,
            name = "pidgey",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/16.png",
            types = listOf("normal", "flying"),
            height = 3,
            weight = 18,
            stats = listOf(
                PokemonStat("hp", 40),
                PokemonStat("attack", 45),
                PokemonStat("defense", 40),
                PokemonStat("special-attack", 35),
                PokemonStat("special-defense", 35),
                PokemonStat("speed", 56)
            ),
            description = "Muito comum em florestas. Ele bate as asas no chão para levantar areia ofuscante."
        ),
        Pokemon(
            id = 94,
            name = "gengar",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/94.png",
            types = listOf("ghost", "poison"),
            height = 15,
            weight = 405,
            stats = listOf(
                PokemonStat("hp", 60),
                PokemonStat("attack", 65),
                PokemonStat("defense", 60),
                PokemonStat("special-attack", 130),
                PokemonStat("special-defense", 75),
                PokemonStat("speed", 110)
            ),
            description = "Diz-se que ele emerge da escuridão para roubar a alma daqueles que se perdem nas montanhas."
        ),
        Pokemon(
            id = 143,
            name = "snorlax",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/143.png",
            types = listOf("normal"),
            height = 21,
            weight = 4600,
            stats = listOf(
                PokemonStat("hp", 160),
                PokemonStat("attack", 110),
                PokemonStat("defense", 65),
                PokemonStat("special-attack", 65),
                PokemonStat("special-defense", 110),
                PokemonStat("speed", 30)
            ),
            description = "Seu estômago pode digerir qualquer tipo de comida, até mesmo comida mofada ou podre."
        ),
        Pokemon(
            id = 150,
            name = "mewtwo",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/150.png",
            types = listOf("psychic"),
            height = 20,
            weight = 1220,
            stats = listOf(
                PokemonStat("hp", 106),
                PokemonStat("attack", 110),
                PokemonStat("defense", 90),
                PokemonStat("special-attack", 154),
                PokemonStat("special-defense", 90),
                PokemonStat("speed", 172)
            ),
            description = "Um Pokémon criado por manipulação genética. É considerado o mais feroz entre os Pokémons."
        )
    )

    fun findById(id: Int): Pokemon? = pokedex.firstOrNull { it.id == id }
}