package io.eisner.urban_search.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(internal val results: SearchResult)

@JsonClass(generateAdapter = true)
data class SearchResult(@Json(name = "trackmatches") internal val trackMatches: SearchMatches)

@JsonClass(generateAdapter = true)
data class SearchMatches(internal val track: List<Track>)