package com.example.movies.model.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
   /* @GET("movie/popular?")
    fun getMovies(@Query("api_key") apiKey: String): Single<MovieResult>*/

    @GET("movie/popular")
    suspend fun getMovies(@Query("api_key") apiKey: String, @Query("page") page: Int)
    : Response<MovieResult>
}