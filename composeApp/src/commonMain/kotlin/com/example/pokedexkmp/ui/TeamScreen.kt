package com.example.pokedexkmp.ui

import androidx.compose.runtime.Composable
import com.example.pokedexkmp.data.Pokemon

// O 'expect' NÃO pode ter corpo (chaves {}). Ele é apenas uma declaração!
@Composable
expect fun TeamScreen(
    team: List<Pokemon>,
    onRemoveFromTeam: (Pokemon) -> Unit
)