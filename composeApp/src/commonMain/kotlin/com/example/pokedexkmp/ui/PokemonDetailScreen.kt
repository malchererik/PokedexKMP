package com.example.pokedexkmp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.pokedexkmp.data.PokeApiClient
import com.example.pokedexkmp.data.PokemonStat

@Composable
fun PokemonDetailScreen(
    pokemon: Pokemon?,
    onBackClick: () -> Unit,
    onAddToTeam: (Pokemon, String) -> Unit,
    isPokemonInTeam: (Int) -> Boolean
) {
    var pokemonApi by remember { mutableStateOf<Pokemon?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // NOVAS VARIÁVEIS PARA O TRATAMENTO DE ERRO
    var hasError by remember { mutableStateOf(false) }
    var retryTrigger by remember { mutableStateOf(0) }

    val apiClient = remember { PokeApiClient() }

    var showDialog by remember { mutableStateOf(false) }
    var captureLocation by remember { mutableStateOf("") }

    // O retryTrigger faz com que a requisição rode novamente ao clicar no botão
    LaunchedEffect(pokemon?.id, retryTrigger) {
        if (pokemon != null) {
            isLoading = true
            hasError = false // Reseta o erro ao tentar de novo
            try {
                val apiData = apiClient.getPokemonDetails(pokemon.id)
                pokemonApi = Pokemon(
                    id = apiData.id,
                    name = apiData.name,
                    imageUrl = apiData.sprites.other?.officialArtwork?.frontDefault ?: pokemon.imageUrl,
                    types = apiData.types.map { it.type.name },
                    height = apiData.height,
                    weight = apiData.weight,
                    stats = apiData.stats.map { PokemonStat(it.stat.name, it.baseStat) },
                    description = "Dados extraídos em Tempo Real da PokeAPI!"
                )
            } catch (e: Exception) {
                println("Erro ao carregar da API: ${e.message}")
                hasError = true // Ativa a tela de erro!
            } finally {
                isLoading = false
            }
        }
    }

    if (showDialog && pokemonApi != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Onde você encontrou o ${pokemonApi!!.name.replaceFirstChar { it.uppercase() }}?") },
            text = {
                OutlinedTextField(
                    value = captureLocation,
                    onValueChange = { captureLocation = it },
                    label = { Text("Local de Captura") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (captureLocation.isNotBlank()) {
                            onAddToTeam(pokemonApi!!, captureLocation)
                            showDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
                ) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            }
        )
    }

    val mainColor = if (pokemonApi != null && !hasError) {
        getPokemonTypeColor(pokemonApi!!.types.firstOrNull() ?: "normal")
    } else {
        Color(0xFFE57373) // Cor padrão caso dê erro ou esteja carregando
    }

    Column(modifier = Modifier.fillMaxSize().background(mainColor)) {
        // CABEÇALHO (Fica sempre visível para o usuário poder voltar)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) { Text("⬅️", fontSize = 24.sp) }
            Text("POKÉDEX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp))
        }

        // TELA DE CARREGAMENTO
        if (isLoading) {
            Surface(
                modifier = Modifier.fillMaxSize().padding(top = 20.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFFE57373))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Buscando detalhes na PokeAPI...")
                    }
                }
            }
            return@Column
        }

        // TELA DE ERRO (O Tentar Novamente)
        if (hasError || pokemonApi == null) {
            Surface(
                modifier = Modifier.fillMaxSize().padding(top = 20.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text("⚠️", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Sem Conexão", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Os detalhes deste Pokémon precisam ser baixados em tempo real da PokeAPI.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { retryTrigger++ }, // Tenta novamente!
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
                    ) {
                        Text("Tentar Novamente", color = Color.White)
                    }
                }
            }
            return@Column
        }

        // --- TELA DE SUCESSO ---
        val currentPokemon = pokemonApi!!
        val isInTeam = isPokemonInTeam(currentPokemon.id)

        Box(modifier = Modifier.fillMaxWidth().height(250.dp), contentAlignment = Alignment.Center) {
            AsyncImage(model = currentPokemon.imageUrl, contentDescription = currentPokemon.name, modifier = Modifier.size(220.dp))
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(text = currentPokemon.id.toString().padStart(3, '0'), fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text(text = "H: ${currentPokemon.height / 10.0} m", color = Color.Gray)
                    Text(text = "W: ${currentPokemon.weight / 10.0} kg", color = Color.Gray)
                }

                Text(text = currentPokemon.name.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 12.dp)) {
                    currentPokemon.types.forEach { type ->
                        Box(modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(getPokemonTypeColor(type)).padding(horizontal = 16.dp, vertical = 6.dp)) {
                            Text(text = translateType(type), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5), RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Text("Status Base", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
                    currentPokemon.stats.forEach { stat ->
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
                Text(text = currentPokemon.description, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = Color.DarkGray)

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { if (!isInTeam) showDialog = true },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isInTeam) Color.Gray else mainColor
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = if (isInTeam) "ADICIONADO" else "ADICIONAR AO TIME",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}