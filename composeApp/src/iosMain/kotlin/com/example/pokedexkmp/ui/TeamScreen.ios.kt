package com.example.pokedexkmp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    // APLICANDO TEMA ESCURO (Dark Scheme) exclusivo para iOS
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color.Black,
            surface = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black) // Fundo preto "True Black" do iOS
                .padding(top = 40.dp)
        ) {

            Text(
                text = "Meu Time",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (team.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum Pokémon no time", color = Color.DarkGray)
                }
            } else {
                LazyColumn {
                    items(team) { pokemon ->
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = Color.Black),
                            headlineContent = {
                                Text(
                                    pokemon.name.replaceFirstChar { it.uppercase() },
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            supportingContent = {
                                Text(
                                    "#${pokemon.id.toString().padStart(3, '0')}",
                                    color = Color.Gray
                                )
                            },
                            leadingContent = {
                                // DIFERENCIAÇÃO: Imagem Circular com Borda (Estilo iOS)
                                AsyncImage(
                                    model = pokemon.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1C1C1E)) // Cinza escuro Apple
                                        .border(1.dp, Color.DarkGray, CircleShape)
                                        .padding(4.dp)
                                )
                            },
                            trailingContent = {
                                // Botão estilo texto (Apple HIG)
                                TextButton(onClick = { onRemoveFromTeam(pokemon) }) {
                                    Text("Remover", color = Color(0xFFFF453A))
                                }
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color(0xFF38383A)
                        )
                    }
                }
            }
        }
    }
}