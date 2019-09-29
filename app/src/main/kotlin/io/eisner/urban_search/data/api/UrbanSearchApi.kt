package io.eisner.urban_search.data.api

import io.eisner.urban_search.data.model.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UrbanSearchApi {
    @GET("define")
    fun searchFor(@Query("term") word: String): Observable<SearchResponse>
}