package com.example.flux.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.flux.data.local.MovieDao
import com.example.flux.data.local.MovieEntity
import com.example.flux.data.remote.CreditsResponse
import com.example.flux.data.remote.MovieDto
import com.example.flux.data.remote.MoviePagingSource
import com.example.flux.data.remote.SearchPagingSource
import com.example.flux.data.remote.TmdbApi
import kotlinx.coroutines.flow.Flow

// Single source of truth for all data operations (Network and Local)
class MovieRepository(
    private val api: TmdbApi,
    private val dao: MovieDao,
    private val apiKey: String
) {
    // Fetches basic details for a specific movie or TV show
    suspend fun getMediaDetails(type: String, id: Int): MovieDto {
        return api.getMediaDetails(type, id, apiKey)
    }

    // Fetches the cast and crew information
    suspend fun getMediaCredits(type: String, id: Int): CreditsResponse {
        return api.getMediaCredits(type, id, apiKey)
    }

    // Creates a Paging flow to handle infinite scrolling for category lists
    fun getMedia(type: String, category: String): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(api, apiKey, type, category) }
        ).flow
    }

    // Creates a Paging flow specifically for search results
    fun searchMedia(type: String, query: String): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(api, apiKey, type, query) }
        ).flow
    }

    // Local Database methods
    fun getMoviesByBucket(bucket: String): Flow<List<MovieEntity>> {
        return dao.getMoviesByBucket(bucket)
    }

    suspend fun insertMovie(movie: MovieEntity) {
        dao.insertMovie(movie)
    }

    suspend fun deleteMovie(movie: MovieEntity) {
        dao.deleteMovie(movie)
    }

    suspend fun isMovieInDatabase(id: Int): Boolean {
        return dao.isMovieInDatabase(id)
    }
}
