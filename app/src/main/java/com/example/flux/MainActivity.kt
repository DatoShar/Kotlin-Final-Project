package com.example.flux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flux.data.local.AppDatabase
import com.example.flux.data.remote.TmdbApi
import com.example.flux.data.repository.MovieRepository
import com.example.flux.ui.navigation.Screen
import com.example.flux.ui.screens.DiscoverScreen
import com.example.flux.ui.screens.ListsScreen
import com.example.flux.ui.screens.MovieDetailsScreen
import com.example.flux.ui.screens.SearchScreen
import com.example.flux.ui.theme.FLUXTheme
import com.example.flux.ui.viewmodel.DiscoverViewModel
import com.example.flux.ui.viewmodel.ListsViewModel
import com.example.flux.ui.viewmodel.MovieDetailsViewModel
import com.example.flux.ui.viewmodel.SearchViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = Retrofit.Builder()
            .baseUrl(TmdbApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)

        val database = AppDatabase.getDatabase(this)
        val repository = MovieRepository(api, database.movieDao(), BuildConfig.TMDB_API_KEY)

        setContent {
            FLUXTheme {
                MainApp(repository)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(repository: MovieRepository) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val discoverViewModel: DiscoverViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return DiscoverViewModel(repository) as T
        }
    })

    val listsViewModel: ListsViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ListsViewModel(repository) as T
        }
    })

    val searchViewModel: SearchViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
    })

    val detailsViewModel: MovieDetailsViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(repository) as T
        }
    })

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(Screen.Discover, Screen.Search, Screen.Lists)
                items.forEach { screen ->
                    val isSelected = currentDestination?.route == screen.route
                    
                    NavigationBarItem(
                        icon = {
                            Icon(
                                when (screen) {
                                    Screen.Discover -> Icons.Default.Search
                                    Screen.Search -> Icons.Default.Search
                                    else -> Icons.AutoMirrored.Filled.List
                                },
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = screen != Screen.Discover
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Discover.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Discover.route) {
                DiscoverScreen(
                    viewModel = discoverViewModel,
                    onMediaClick = { type, id ->
                        navController.navigate(Screen.Details.createRoute(type, id))
                    }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    viewModel = searchViewModel,
                    onMediaClick = { type, id ->
                        navController.navigate(Screen.Details.createRoute(type, id))
                    }
                )
            }
            composable(Screen.Lists.route) {
                ListsScreen(
                    viewModel = listsViewModel,
                    onMediaClick = { type, id ->
                        navController.navigate(Screen.Details.createRoute(type, id))
                    }
                )
            }
            composable(Screen.Details.route) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: "movie"
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                MovieDetailsScreen(
                    mediaType = type,
                    mediaId = id,
                    viewModel = detailsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
