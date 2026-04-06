package com.example.pokedexkmp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pokedexkmp.data.Pokemon

@Composable
fun TeamScreen(
    team: List<Pokemon>,
    onRemoveFromTeam: (Pokemon) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "MEU TIME",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        Text(
            text = "${team.size} / 6 Pokémons",
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (team.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Seu time está vazio!", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(team) { pokemon ->
                    TeamGridItem(pokemon = pokemon, onRemove = { onRemoveFromTeam(pokemon) })
                }
            }
        }
    }
}

@Composable
private fun TeamGridItem(
    pokemon: Pokemon,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(90.dp)
            )

            Text(
                text = pokemon.name.capitalizePokemonName(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botão de Remover
            Button(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth().height(36.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)), // Vermelho
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "REMOVER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}