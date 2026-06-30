package com.example.flux.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.flux.ui.components.MovieCard
import com.example.flux.ui.viewmodel.DiscoverViewModel

// Main browsing screen with categories and infinite grid
@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    onMediaClick: (String, Int) -> Unit
) {
    val mediaItems = viewModel.mediaFlow.collectAsLazyPagingItems()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()

    val categories = if (selectedType == "movie") {
        listOf(
            "Popular" to "popular",
            "Top Rated" to "top_rated",
            "Upcoming" to "upcoming"
        )
    } else {
        listOf(
            "Popular" to "popular",
            "Top Rated" to "top_rated",
            "On The Air" to "on_the_air"
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Media Type Selector (Movies/TV)
        TabRow(selectedTabIndex = if (selectedType == "movie") 0 else 1) {
            Tab(
                selected = selectedType == "movie",
                onClick = { viewModel.setType("movie") },
                text = { Text("Movies") }
            )
            Tab(
                selected = selectedType == "tv",
                onClick = { viewModel.setType("tv") },
                text = { Text("TV Shows") }
            )
        }

        // Selection chips for different movie categories
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { (title, slug) ->
                FilterChip(
                    selected = selectedCategory == slug,
                    onClick = { viewModel.setCategory(slug) },
                    label = { Text(title) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        // Infinite scrolling 2-column grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(mediaItems.itemCount) { index ->
                mediaItems[index]?.let { item ->
                    MovieCard(
                        movie = item,
                        onBucketClick = { bucket ->
                            viewModel.addMediaToBucket(item, bucket)
                        },
                        onClick = { onMediaClick(selectedType, item.id) }
                    )
                }
            }
        }
    }
}
