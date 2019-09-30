package io.eisner.urban_search.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(val list: List<UrbanDefinition>)