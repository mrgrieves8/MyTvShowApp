package com.example.tvrawr.data.remote_db

import com.example.tvrawr.data.models.TvShow
import retrofit2.http.GET
import retrofit2.http.Path

interface TvShowRemoteDataSource {

    @GET("tvshows")
    suspend fun getTvShows(): List<TvShow>

    @GET("tvshows/{id}")
    suspend fun getTvShow(@Path("id") id: Int): TvShow
}
