package com.example.flux.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.flux.ui.components.MovieCard
import com.example.flux.ui.viewmodel.SearchViewModel

// Screen for searching movies across the entire TMDB database
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onMediaClick: (String, Int) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        // Media Type Selector
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

        // Debounced search input field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        // Result grid: Automatically updates as the user types (after debounce)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(searchResults.itemCount) { index ->
                searchResults[index]?.let { item ->
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
