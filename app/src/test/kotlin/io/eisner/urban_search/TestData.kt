package io.eisner.urban_search

import io.eisner.urban_search.data.model.SearchMatches
import io.eisner.urban_search.data.model.SearchResponse
import io.eisner.urban_search.data.model.SearchResult
import io.eisner.urban_search.data.model.Track

val testList = listOf(
    Track("Something to test", "Testers", 1, ""),
    Track("Something to test", "Testers", 0, "")
)

val testResponse = SearchResponse(SearchResult(SearchMatches(testList)))