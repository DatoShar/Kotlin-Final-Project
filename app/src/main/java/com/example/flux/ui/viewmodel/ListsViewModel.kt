package com.example.flux.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flux.data.local.MovieEntity
import com.example.flux.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListsViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _watchlistMovies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val watchlistMovies: StateFlow<List<MovieEntity>> = _watchlistMovies

    private val _watchedMovies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val watchedMovies: StateFlow<List<MovieEntity>> = _watchedMovies

    init {
        fetchMoviesByBucket("WATCHLIST", _watchlistMovies)
        fetchMoviesByBucket("WATCHED", _watchedMovies)
    }

    private fun fetchMoviesByBucket(bucket: String, flow: MutableStateFlow<List<MovieEntity>>) {
        viewModelScope.launch {
            repository.getMoviesByBucket(bucket).collectLatest {
                flow.value = it
            }
        }
    }

    fun deleteMovie(movie: MovieEntity) {
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }
}
