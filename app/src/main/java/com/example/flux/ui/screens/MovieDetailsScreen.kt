package com.example.flux.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flux.data.remote.TmdbApi
import com.example.flux.ui.viewmodel.MovieDetailsUiState
import com.example.flux.ui.viewmodel.MovieDetailsViewModel

// Premium screen for viewing media details and cast
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    mediaType: String,
    mediaId: Int,
    viewModel: MovieDetailsViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Trigger data fetch when the screen is first loaded
    LaunchedEffect(mediaId, mediaType) {
        viewModel.fetchDetails(mediaType, mediaId)
    }

    Scaffold(
        floatingActionButton = {
            // Share FAB using native Android Intent
            if (uiState is MovieDetailsUiState.Success) {
                val movie = (uiState as MovieDetailsUiState.Success).movie
                FloatingActionButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, movie.displayTitle)
                            putExtra(Intent.EXTRA_TEXT, "Check out ${movie.displayTitle}! It has a rating of ${String.format("%.1f", movie.voteAverage)}.")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share"))
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (uiState) {
                is MovieDetailsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MovieDetailsUiState.Error -> {
                    Text(
                        text = (uiState as MovieDetailsUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is MovieDetailsUiState.Success -> {
                    val data = uiState as MovieDetailsUiState.Success
                    val movie = data.movie
                    val cast = data.cast

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Header Image: Backdrop with correct aspect ratio to prevent cropping
                        AsyncImage(
                            model = TmdbApi.IMAGE_BASE_URL + (movie.backdropPath ?: movie.posterPath),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f), // Using wide aspect ratio for high-quality backdrops
                            contentScale = ContentScale.Crop // Fill the space nicely
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = movie.displayTitle,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Visual Metadata Row (Release Year & Rating)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Release: ${movie.displayDate}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "⭐ ${String.format("%.1f", movie.voteAverage)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Overview",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = movie.overview,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = 22.sp
                            )
                            
                            // Cast Section
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Cast",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(cast) { person ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(100.dp)
                                    ) {
                                        AsyncImage(
                                            model = TmdbApi.IMAGE_BASE_URL + person.profilePath,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            text = person.name,
                                            style = MaterialTheme.typography.labelMedium,
                                            maxLines = 2,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
