package com.example.tvrawr.data.models


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class IMDBGenericEntry(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String,
)

data class IMDBDetailEntry(
    val Title: String,
    val Year: String,
    val Rated: String,
    val Released: String,
    val Runtime: String,
    val Genre: String,
    val Director: String,
    val Writer: String,
    val Actors: String,
    var Plot: String,
    val Language: String,
    val Country: String,
    val Awards: String,
    val Poster: String,
    val Ratings: List<IMDBRating>,
    val Metascore: String,
    val imdbRating: String,
    val imdbVotes: String,
    val imdbID: String,
    val Type: String,
    val totalSeasons: String,
    val Response: String
)

data class IMDBRating(
    val Source: String,
    val Value: String
)


data class IMDBFullTextResponse(val Search: List<IMDBGenericEntry>)


interface OMDbService {

    // API Key must be last parameter
    @GET("/?&type=series&apikey=ec057198")
    suspend fun byFullText(
        @Query("s") searchTerm: String
    ): IMDBFullTextResponse

    @GET("/?&type=series&apikey=ec057198")
    suspend fun byTitle(
        @Query("i") searchTerm: String
    ): IMDBDetailEntry
}


object OMDbAPI {
    private const val BASE_URL = "https://www.omdbapi.com/"
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: OMDbService = retrofit.create(OMDbService::class.java)
}