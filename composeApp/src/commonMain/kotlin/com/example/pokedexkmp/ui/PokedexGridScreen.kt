package com.example.pokedexkmp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.pokedexkmp.data.Pokemon
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun PokedexGridScreen(
    pokemons: List<Pokemon>,
    onPokemonClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onAddToTeam: (Pokemon) -> Unit,
    isPokemonInTeam: (Int) -> Boolean,
    onSearch: (String) -> Unit,
    onLoadMore: () -> Unit // NOVO: A tela agora pode pedir mais dados!
) {
    var searchQuery by remember { mutableStateOf("") }
    val gridState = rememberLazyGridState()

    // O "Olheiro" do Scroll Infinito
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .filter { lastIndex -> lastIndex != null && lastIndex >= pokemons.size - 4 } // Quando faltar 4 itens para o fim
            .collect { onLoadMore() } // Chama a função para baixar a próxima página
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFE57373)).padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) { Text("⬅️", fontSize = 24.sp) }
            Text("POKÉDEX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp))
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it)
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text("Buscar Pokémon no Banco...") },
            shape = RoundedCornerShape(25.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedBorderColor = Color(0xFFE57373)
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState, // Liga a lista ao nosso "Olheiro"
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
                        Text(text = pokemon.id.toString().padStart(3, '0'), color = Color.Gray, fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
                        AsyncImage(model = pokemon.imageUrl, contentDescription = pokemon.name, modifier = Modifier.size(100.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = pokemon.name.replaceFirstChar { it.uppercase() }, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}