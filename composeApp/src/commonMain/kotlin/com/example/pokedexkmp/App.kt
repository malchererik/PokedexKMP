package com.example.pokedexkmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokedexkmp.data.Pokemon
import com.example.pokedexkmp.data.PokemonMock
import com.example.pokedexkmp.navigation.PokedexRoute
import com.example.pokedexkmp.navigation.PokemonDetailRoute
import com.example.pokedexkmp.navigation.TeamRoute
import com.example.pokedexkmp.ui.PokedexGridScreen
import com.example.pokedexkmp.ui.PokemonDetailScreen
import com.example.pokedexkmp.ui.TeamScreen

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // A Mochila do Time
        val myTeam = remember { mutableStateListOf<Pokemon>() }

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
                        PokedexGridScreen(
                            pokemons = PokemonMock.pokedex,
                            onPokemonClick = { pokemonId ->
                                navController.navigate(PokemonDetailRoute(pokemonId))
                            },
                            onBackClick = { navController.popBackStack() },
                            onAddToTeam = { pokemon ->
                                if (myTeam.size < 6 && !myTeam.any { it.id == pokemon.id }) {
                                    myTeam.add(pokemon)
                                }
                            },
                            // NOVO: Verifica se o Pokémon já está no time
                            isPokemonInTeam = { pokemonId ->
                                myTeam.any { it.id == pokemonId }
                            }
                        )
                    }

                    composable<PokemonDetailRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<PokemonDetailRoute>()
                        val pokemon = PokemonMock.findById(route.pokemonId)

                        PokemonDetailScreen(
                            pokemon = pokemon,
                            onBackClick = { navController.popBackStack() },
                            onAddToTeam = { p ->
                                if (myTeam.size < 6 && p != null && !myTeam.any { it.id == p.id }) {
                                    myTeam.add(p)
                                }
                            },
                            // NOVO: Verifica se o Pokémon já está no time
                            isPokemonInTeam = { pokemonId ->
                                myTeam.any { it.id == pokemonId }
                            }
                        )
                    }

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