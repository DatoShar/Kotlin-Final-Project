package com.example.flux.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val voteAverage: Double,
    val bucket: String, // "WATCHLIST" or "WATCHED"
    val mediaType: String = "movie",
    val releaseDate: String? = null
)
