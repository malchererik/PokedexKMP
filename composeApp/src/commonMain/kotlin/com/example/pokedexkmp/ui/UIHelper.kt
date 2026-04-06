package com.example.pokedexkmp.ui

import androidx.compose.ui.graphics.Color

fun String.capitalizePokemonName(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

fun Int.formatPokemonNumber(): String = "#${toString().padStart(3, '0')}"

fun getPokemonTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "grass" -> Color(0xFF4CAF50)
        "poison" -> Color(0xFF9C27B0)
        "fire" -> Color(0xFFF44336)
        "water" -> Color(0xFF2196F3)
        "electric" -> Color(0xFFFFEB3B)
        "normal" -> Color(0xFF9E9E9E)
        "flying" -> Color(0xFF03A9F4)
        "ghost" -> Color(0xFF673AB7)
        "psychic" -> Color(0xFFE91E63)
        "fairy" -> Color(0xFFFF4081)
        else -> Color(0xFFBDBDBD)
    }
}

fun getStatColor(statName: String): Color {
    return when (statName.lowercase()) {
        "hp" -> Color(0xFF4CAF50)
        "attack" -> Color(0xFFF44336)
        "defense" -> Color(0xFFFF9800)
        "special-attack" -> Color(0xFF2196F3)
        "special-defense" -> Color(0xFF8BC34A)
        "speed" -> Color(0xFFE91E63)
        else -> Color(0xFF9E9E9E)
    }
}

// NOVO: Função para traduzir os tipos para o Português
fun translateType(type: String): String {
    return when (type.lowercase()) {
        "grass" -> "PLANTA"
        "poison" -> "VENENO"
        "fire" -> "FOGO"
        "water" -> "ÁGUA"
        "electric" -> "ELÉTRICO"
        "normal" -> "NORMAL"
        "flying" -> "VOADOR"
        "ghost" -> "FANTASMA"
        "psychic" -> "PSÍQUICO"
        "fairy" -> "FADA"
        else -> type.uppercase()
    }
}

// NOVO: Função para traduzir as barrinhas de status
fun translateStat(stat: String): String {
    return when (stat.lowercase()) {
        "hp" -> "HP"
        "attack" -> "Ataque"
        "defense" -> "Defesa"
        "special-attack" -> "Atq. Esp."
        "special-defense" -> "Def. Esp."
        "speed" -> "Velocidade"
        else -> stat.capitalizePokemonName()
    }
}