package com.example.pokedexkmp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.database.AppDatabase
import com.example.pokedexkmp.navigation.PokedexRoute
import com.example.pokedexkmp.navigation.PokemonDetailRoute
import com.example.pokedexkmp.navigation.TeamRoute
import com.example.pokedexkmp.presentation.PokedexUiState
import com.example.pokedexkmp.presentation.PokedexViewModel
import com.example.pokedexkmp.repository.PokemonRepositoryImpl
import com.example.pokedexkmp.ui.PokedexGridScreen
import com.example.pokedexkmp.ui.PokemonDetailScreen
import com.example.pokedexkmp.ui.TeamScreen

@Composable
fun App(database: AppDatabase) {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val myTeam = remember { mutableStateListOf<Pokemon>() }
        val pokemonRepository = remember { PokemonRepositoryImpl(database) }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<PokedexRoute>() } == true,
                        onClick = {
                            navController.navigate(PokedexRoute) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text("Pokédex") },
                        icon = { Text("📋") }
                    )

                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<TeamRoute>() } == true,
                        onClick = {
                            navController.navigate(TeamRoute) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text("Meu Time") },
                        icon = { Text("🎒") }
                    )
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(navController = navController, startDestination = PokedexRoute) {

                    // ==========================================
                    // TELA 1: A POKÉDEX (COM VIEWMODEL E SYNC!)
                    // ==========================================
                    composable<PokedexRoute> {
                        // O ViewModel "nasce" aqui e dispara a sincronização (Sync)
                        val viewModel = viewModel { PokedexViewModel(pokemonRepository) }
                        val uiState by viewModel.uiState.collectAsState()

                        when (val state = uiState) {
                            is PokedexUiState.Loading -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator()
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text("Sincronizando Banco de Dados...")
                                    }
                                }
                            }
                            is PokedexUiState.Success -> {
                                PokedexGridScreen(
                                    pokemons = state.pokemons, // Lê os dados reais do banco
                                    onPokemonClick = { pokemonId ->
                                        navController.navigate(PokemonDetailRoute(pokemonId))
                                    },
                                    onBackClick = { navController.popBackStack() },
                                    onAddToTeam = { pokemon ->
                                        if (myTeam.size < 6 && !myTeam.any { it.id == pokemon.id }) {
                                            myTeam.add(pokemon)
                                        }
                                    },
                                    isPokemonInTeam = { pokemonId ->
                                        myTeam.any { it.id == pokemonId }
                                    }
                                )
                            }
                            is PokedexUiState.Error -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Erro: ${state.message}", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }

                    // ==========================================
                    // TELA 2: OS DETALHES (CHAMANDO A API)
                    // ==========================================
                    composable<PokemonDetailRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<PokemonDetailRoute>()

                        // Criamos um Pokémon falso só com o ID para a API saber quem buscar!
                        val dummyPokemon = Pokemon(
                            id = route.pokemonId,
                            name = "Carregando...",
                            imageUrl = "",
                            types = emptyList(), height = 0, weight = 0, stats = emptyList(), description = ""
                        )

                        PokemonDetailScreen(
                            pokemon = dummyPokemon,
                            onBackClick = { navController.popBackStack() },
                            onAddToTeam = { p ->
                                if (myTeam.size < 6 && p != null && !myTeam.any { it.id == p.id }) {
                                    myTeam.add(p)
                                }
                            },
                            isPokemonInTeam = { pokemonId ->
                                myTeam.any { it.id == pokemonId }
                            }
                        )
                    }

                    // ==========================================
                    // TELA 3: O TIME
                    // ==========================================
                    composable<TeamRoute> {
                        TeamScreen(
                            team = myTeam,
                            onRemoveFromTeam = { pokemon ->
                                myTeam.remove(pokemon)
                            }
                        )
                    }
                }
            }
        }
    }
}