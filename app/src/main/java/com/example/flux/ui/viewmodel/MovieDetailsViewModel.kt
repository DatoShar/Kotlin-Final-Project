package com.example.flux.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flux.data.remote.CastDto
import com.example.flux.data.remote.MovieDto
import com.example.flux.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MovieDetailsUiState {
    object Loading : MovieDetailsUiState()
    data class Success(val movie: MovieDto, val cast: List<CastDto>) : MovieDetailsUiState()
    data class Error(val message: String) : MovieDetailsUiState()
}

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState

    fun fetchDetails(type: String, id: Int) {
        viewModelScope.launch {
            _uiState.value = MovieDetailsUiState.Loading
            try {
                val movie = repository.getMediaDetails(type, id)
                val credits = repository.getMediaCredits(type, id)
                _uiState.value = MovieDetailsUiState.Success(movie, credits.cast)
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
