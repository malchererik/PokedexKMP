package com.example.pokedexkmp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun PokemonDetailScreen(
    pokemon: Pokemon?,
    onBackClick: () -> Unit,
    onAddToTeam: (Pokemon) -> Unit,
    isPokemonInTeam: (Int) -> Boolean
) {
    if (pokemon == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Pokémon não encontrado.") }
        return
    }

    val mainColor = getPokemonTypeColor(pokemon.types.firstOrNull() ?: "normal")
    // Verifica se este Pokémon atual já está no time
    val isInTeam = isPokemonInTeam(pokemon.id)

    Column(modifier = Modifier.fillMaxSize().background(mainColor)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) { Text("⬅️", fontSize = 24.sp) }
            Text("POKÉDEX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp))
        }

        Box(modifier = Modifier.fillMaxWidth().height(250.dp), contentAlignment = Alignment.Center) {
            AsyncImage(model = pokemon.imageUrl, contentDescription = pokemon.name, modifier = Modifier.size(220.dp))
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(text = pokemon.id.formatPokemonNumber(), fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text(text = "H: ${pokemon.height / 10.0} m", color = Color.Gray)
                    Text(text = "W: ${pokemon.weight / 10.0} kg", color = Color.Gray)
                }

                Text(text = pokemon.name.capitalizePokemonName(), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 12.dp)) {
                    pokemon.types.forEach { type ->
                        Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(getPokemonTypeColor(type)).padding(horizontal = 16.dp, vertical = 6.dp)) {
                            Text(text = translateType(type), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Text("Status Base", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                    pokemon.stats.forEach { stat ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = translateStat(stat.name), modifier = Modifier.weight(0.25f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = stat.value.toString(), modifier = Modifier.weight(0.15f), fontSize = 14.sp, textAlign = TextAlign.End)
                            Spacer(modifier = Modifier.width(8.dp))
                            LinearProgressIndicator(
                                progress = { stat.value / 150f },
                                modifier = Modifier.weight(0.6f).height(8.dp).clip(RoundedCornerShape(4.dp)),
                                color = getStatColor(stat.name), trackColor = Color(0xFFE0E0E0),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = pokemon.description, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = Color.DarkGray)
                Spacer(modifier = Modifier.weight(1f))

                // BOTÃO ALTERADO AQUI!
                Button(
                    onClick = { if (!isInTeam) onAddToTeam(pokemon) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isInTeam) Color.Gray else mainColor // Fica cinza se adicionado
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = if (isInTeam) "ADICIONADO" else "ADICIONAR AO TIME", // Muda o texto
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}