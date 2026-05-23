package com.example.pokedexkmp.ui

import androidx.compose.runtime.Composable
import com.example.pokedexkmp.data.Pokemon

@Composable
expect fun TeamScreen(
    team: List<Pokemon>,
    onRemoveFromTeam: (Pokemon) -> Unit
)