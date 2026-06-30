package com.example.flux.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Data Access Object for managing local movie storage
@Dao
interface MovieDao {
    // Retrieves movies filtered by their status (Watchlist, Watched, etc.)
    @Query("SELECT * FROM movies WHERE bucket = :bucket")
    fun getMoviesByBucket(bucket: String): Flow<List<MovieEntity>>

    // Saves or updates a movie in the local database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    // Removes a movie from local storage
    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    // Checks if a specific movie is already saved locally
    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :id)")
    suspend fun isMovieInDatabase(id: Int): Boolean
}
