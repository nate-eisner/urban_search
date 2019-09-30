package io.eisner.urban_search

import io.eisner.urban_search.data.model.SearchResponse
import io.eisner.urban_search.data.model.UrbanDefinition

val testList = listOf<UrbanDefinition>(
    UrbanDefinition(1, "Something to test", "test", 10, 10),
    UrbanDefinition(2, "Something to test", "test", 9, 7)
)

val testResponse = SearchResponse(testList)