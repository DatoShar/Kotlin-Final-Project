package com.example.flux.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flux.data.local.MovieEntity
import com.example.flux.ui.components.MovieCard
import com.example.flux.ui.viewmodel.ListsViewModel
import com.example.flux.data.remote.MovieDto
import androidx.compose.ui.Alignment

/**
 * Screen for managing personal movie/TV lists saved in Room.
 * Simplified UI: Removed dropdowns and added a direct "Remove" button for items.
 */
@Composable
fun ListsScreen(
    viewModel: ListsViewModel,
    onMediaClick: (String, Int) -> Unit
) {
    val watchlist by viewModel.watchlistMovies.collectAsState()
    val watched by viewModel.watchedMovies.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Watchlist", "Watched")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        val currentList = when (selectedTab) {
            0 -> watchlist
            else -> watched
        }

        if (currentList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No items here yet!")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(currentList) { entity ->
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        val movieDto = MovieDto(
                            id = entity.id,
                            title = entity.title,
                            name = null,
                            overview = entity.overview,
                            posterPath = entity.posterPath,
                            backdropPath = null,
                            voteAverage = entity.voteAverage,
                            releaseDate = entity.releaseDate,
                            firstAirDate = null
                        )
                        
                        // Use MovieCard without action buttons/dropdowns
                        MovieCard(
                            movie = movieDto,
                            showActions = false, 
                            onClick = { onMediaClick(entity.mediaType, entity.id) }
                        )
                        
                        // Single "Remove" button placed clearly below the card
                        Button(
                            onClick = { viewModel.deleteMovie(entity) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Remove from ${if (selectedTab == 0) "Watchlist" else "Watched"}")
                        }
                    }
                }
            }
        }
    }
}
