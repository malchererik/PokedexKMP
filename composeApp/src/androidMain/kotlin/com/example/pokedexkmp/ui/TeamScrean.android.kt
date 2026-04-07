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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pokedexkmp.data.Pokemon

@Composable
actual fun TeamScreen(
    team: List<Pokemon>,
    onRemoveFromTeam: (Pokemon) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text(
            text = "MEU TIME",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (team.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("A sua mochila está vazia!", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(team) { pokemon ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = pokemon.imageUrl,
                                contentDescription = pokemon.name,
                                modifier = Modifier.size(80.dp)
                            )
                            Text(
                                text = pokemon.name.uppercase(),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                            Button(
                                onClick = { onRemoveFromTeam(pokemon) },
                                modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("REMOVER", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}