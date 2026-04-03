package com.example.pokedexkmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pokedexkmp.data.PokemonMock
import com.example.pokedexkmp.navigation.PokedexRoute
import com.example.pokedexkmp.navigation.PokemonDetailRoute
import com.example.pokedexkmp.navigation.TeamRoute
import com.example.pokedexkmp.ui.PokedexGridScreen
import com.example.pokedexkmp.ui.PokemonDetailScreen

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

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
                NavHost(
                    navController = navController,
                    startDestination = PokedexRoute
                ) {
                    composable<PokedexRoute> {
                        PokedexGridScreen(
                            pokemons = PokemonMock.pokedex,
                            onPokemonClick = { pokemonId ->
                                navController.navigate(PokemonDetailRoute(pokemonId))
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<PokemonDetailRoute> { backStackEntry ->
                        val route = backStackEntry.toRoute<PokemonDetailRoute>()
                        val pokemon = PokemonMock.findById(route.pokemonId)

                        PokemonDetailScreen(
                            pokemon = pokemon,
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable<TeamRoute> {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Meu Time Pokémon",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = "Aqui aparecerão os Pokémons do seu time.",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}