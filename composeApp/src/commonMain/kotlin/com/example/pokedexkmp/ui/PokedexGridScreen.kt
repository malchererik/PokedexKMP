package com.example.pokedexkmp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexGridScreen(
    pokemons: List<Pokemon>,
    onPokemonClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onAddToTeam: (Pokemon) -> Unit,
    isPokemonInTeam: (Int) -> Boolean
) {
    // Agora precisamos apenas da variável de texto!
    var searchQuery by remember { mutableStateOf("") }

    // Lógica de filtragem original (que já estava perfeita)
    val filteredPokemons = remember(searchQuery, pokemons) {
        pokemons.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.id.toString().contains(searchQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "POKÉDEX KMP",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        // SearchBar corrigida para não cobrir a tela!
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { }, // Não precisa fazer nada, o filtro é em tempo real
            active = false, // O SEGREDO ESTÁ AQUI: Impede a barra de cobrir a tela inteira!
            onActiveChange = { },
            placeholder = { Text("Buscar por nome ou número...") },
            leadingIcon = { Text("🔍", fontSize = 18.sp, modifier = Modifier.padding(start = 12.dp)) },
            trailingIcon = {
                // NOVO: Botão de limpar pesquisa (X) que aparece quando você digita algo
                if (searchQuery.isNotEmpty()) {
                    Text(
                        text = "❌",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { searchQuery = "" } // Limpa e volta a lista completa
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = SearchBarDefaults.colors(containerColor = Color.White)
        ) {
            // Deixamos vazio pois o active é sempre false
        }

        // Grid utilizando a lista filtrada
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredPokemons) { pokemon ->
                PokemonGridItem(
                    pokemon = pokemon,
                    onClick = { onPokemonClick(pokemon.id) },
                    onAddClick = { onAddToTeam(pokemon) },
                    isInTeam = isPokemonInTeam(pokemon.id)
                )
            }
        }
    }
}

@Composable
private fun PokemonGridItem(
    pokemon: Pokemon,
    onClick: () -> Unit,
    onAddClick: () -> Unit,
    isInTeam: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = pokemon.imageUrl,
                    contentDescription = pokemon.name,
                    modifier = Modifier.size(90.dp)
                )
            }

            Text(
                text = pokemon.name.capitalizePokemonName(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Text(
                text = pokemon.id.formatPokemonNumber(),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().height(26.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                pokemon.types.forEach { type ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(getPokemonTypeColor(type))
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = translateType(type),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { if (!isInTeam) onAddClick() },
                modifier = Modifier.fillMaxWidth().height(36.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInTeam) Color.Gray else Color(0xFF81C784)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = if (isInTeam) "ADICIONADO" else "ADICIONAR AO TIME",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}