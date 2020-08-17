package io.eisner.urban_search.data.api

import io.eisner.urban_search.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UrbanSearchApi {
    @GET("?method=track.search&format=json")
    suspend fun searchFor(@Query("track") word: String): SearchResponse
}