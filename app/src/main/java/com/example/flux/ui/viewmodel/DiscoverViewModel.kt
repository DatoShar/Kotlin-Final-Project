package com.example.flux.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flux.data.local.MovieEntity
import com.example.flux.data.remote.MovieDto
import com.example.flux.data.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Manages state for the browsing screen
class DiscoverViewModel(private val repository: MovieRepository) : ViewModel() {

    // Reactive state for the currently selected media type (movie or tv)
    val selectedType = MutableStateFlow("movie")
    
    // Reactive state for the currently selected movie category
    val selectedCategory = MutableStateFlow("popular")

    // Dynamic media stream that refreshes whenever type or category changes
    @OptIn(ExperimentalCoroutinesApi::class)
    val mediaFlow: Flow<PagingData<MovieDto>> = combine(selectedType, selectedCategory) { type, category ->
        type to category
    }.flatMapLatest { (type, category) ->
        repository.getMedia(type, category)
    }.cachedIn(viewModelScope)

    fun setType(type: String) {
        selectedType.value = type
        selectedCategory.value = "popular" // Reset category to avoid invalid combos
    }

    fun setCategory(category: String) {
        selectedCategory.value = category
    }

    fun addMediaToBucket(movie: MovieDto, bucket: String) {
        viewModelScope.launch {
            repository.insertMovie(
                MovieEntity(
                    id = movie.id,
                    title = movie.displayTitle,
                    overview = movie.overview,
                    posterPath = movie.posterPath,
                    voteAverage = movie.voteAverage,
                    bucket = bucket,
                    mediaType = selectedType.value,
                    releaseDate = movie.displayDate
                )
            )
        }
    }
}
