package com.example.pokedexkmp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
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
import com.example.pokedexkmp.database.TeamPokemonEntity
import com.example.pokedexkmp.navigation.PokedexRoute
import com.example.pokedexkmp.navigation.PokemonDetailRoute
import com.example.pokedexkmp.navigation.TeamRoute
import com.example.pokedexkmp.presentation.PokedexUiState
import com.example.pokedexkmp.presentation.PokedexViewModel
import com.example.pokedexkmp.repository.PokemonRepositoryImpl
import com.example.pokedexkmp.ui.PokedexGridScreen
import com.example.pokedexkmp.ui.PokemonDetailScreen
import com.example.pokedexkmp.ui.TeamScreen
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@Composable
fun App(database: AppDatabase) {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val pokemonRepository = remember { PokemonRepositoryImpl(database) }
        val coroutineScope = rememberCoroutineScope()

        val teamEntities by database.pokemonDao().getTeam().collectAsState(initial = emptyList())
        val myTeam = remember(teamEntities) {
            teamEntities.map { entity ->
                Pokemon(
                    id = entity.id,
                    name = entity.name,
                    imageUrl = entity.imageUrl,
                    types = emptyList(), height = 0, weight = 0, stats = emptyList(), description = "",
                    captureLocation = entity.captureLocation
                )
            }
        }

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

                    composable<PokedexRoute> {
                        val viewModel = viewModel<PokedexViewModel>(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                                    return PokedexViewModel(pokemonRepository) as T
                                }
                            }
                        )

                        val uiState by viewModel.uiState.collectAsState()

                        when (val state = uiState) {
                            is PokedexUiState.Loading -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator()
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text("Carregando Pokédex...")
                                    }
                                }
                            }
                            is PokedexUiState.Success -> {
                                PokedexGridScreen(
                                    pokemons = state.pokemons,
                                    onPokemonClick = { pokemonId -> navController.navigate(PokemonDetailRoute(pokemonId)) },
                                    onBackClick = { navController.popBackStack() },
                                    onAddToTeam = { _ -> },
                                    isPokemonInTeam = { pokemonId -> myTeam.any { it.id == pokemonId } },
                                    onSearch = { query -> viewModel.onSearchQueryChanged(query) },
                                    onLoadMore = { viewModel.loadMorePokemons() }
                                )
                            }
                            is PokedexUiState.Error -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Erro: ${state.message}")
                                }
                            }
                        }
                    }

                    composable<PokemonDetailRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<PokemonDetailRoute>()
                        val dummyPokemon = Pokemon(id = route.pokemonId, name = "Carregando...", imageUrl = "", types = emptyList(), height = 0, weight = 0, stats = emptyList(), description = "")

                        PokemonDetailScreen(
                            pokemon = dummyPokemon,
                            onBackClick = { navController.popBackStack() },
                            onAddToTeam = { p, location ->
                                if (myTeam.size < 6 && !myTeam.any { it.id == p.id }) {
                                    coroutineScope.launch {
                                        database.pokemonDao().addToTeam(
                                            TeamPokemonEntity(
                                                id = p.id,
                                                name = p.name,
                                                imageUrl = p.imageUrl,
                                                captureLocation = location
                                            )
                                        )
                                    }
                                }
                            },
                            isPokemonInTeam = { pokemonId -> myTeam.any { it.id == pokemonId } }
                        )
                    }

                    composable<TeamRoute> {
                        TeamScreen(
                            team = myTeam,
                            onRemoveFromTeam = { pokemon ->
                                coroutineScope.launch {
                                    database.pokemonDao().removeFromTeam(pokemon.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}