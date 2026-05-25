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

@Composable
fun PokedexGridScreen(
    pokemons: List<Pokemon>,
    onPokemonClick: (Int) -> Unit, // <-- CORREÇÃO: Agora espera o Int correto do ID!
    onBackClick: () -> Unit,
    onAddToTeam: (Pokemon) -> Unit,
    isPokemonInTeam: (Int) -> Boolean,
    onSearch: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE57373))
                .padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) { Text("⬅️", fontSize = 24.sp) }
            Text("POKÉDEX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp))
        }

        // BARRA DE PESQUISA (SearchBar)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it) // Dispara a busca SQL!
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text("Buscar Pokémon no Banco...") },
            shape = RoundedCornerShape(25.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedBorderColor = Color(0xFFE57373)
            )
        )

        // GRID DE POKÉMONS
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(pokemons) { pokemon ->
                Card(
                    modifier = Modifier.padding(8.dp).fillMaxWidth().height(180.dp).clickable { onPokemonClick(pokemon.id) },
                    shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(8.dp)) {
                        Text(text = pokemon.id.formatPokemonNumber(), color = Color.Gray, fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
                        AsyncImage(model = pokemon.imageUrl, contentDescription = pokemon.name, modifier = Modifier.size(100.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = pokemon.name.capitalizePokemonName(), fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}