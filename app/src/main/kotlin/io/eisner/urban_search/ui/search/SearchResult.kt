package io.eisner.urban_search.ui.search

import io.eisner.urban_search.data.Source
import io.eisner.urban_search.data.model.Track

sealed class SearchResult {
    object Error : SearchResult()
    object Loading : SearchResult()
    data class Data(val source: Source, val data: List<Track>) : SearchResult()
}