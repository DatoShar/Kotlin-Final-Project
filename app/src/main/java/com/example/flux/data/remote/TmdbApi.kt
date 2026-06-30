package com.example.flux.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("{type}/{category}")
    suspend fun getMedia(
        @Path("type") type: String, // "movie" or "tv"
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET("search/{type}")
    suspend fun searchMedia(
        @Path("type") type: String, // "movie" or "tv"
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET("{type}/{id}")
    suspend fun getMediaDetails(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieDto

    @GET("{type}/{id}/credits")
    suspend fun getMediaCredits(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): CreditsResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}
