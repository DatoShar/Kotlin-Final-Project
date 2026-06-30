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

// Handles real-time search logic and coordination with TMDB API
class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    val selectedType = MutableStateFlow("movie")
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Reactive flow for search results. 
    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: Flow<PagingData<MovieDto>> = combine(selectedType, _searchQuery) { type, query ->
        type to query
    }.debounce(500)
    .distinctUntilChanged()
    .flatMapLatest { (type, query) ->
        repository.searchMedia(type, query)
    }.cachedIn(viewModelScope)

    fun setType(type: String) {
        selectedType.value = type
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // Persists a movie from search results into a specific local category
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
