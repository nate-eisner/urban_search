package io.eisner.urban_search.ui.search

import io.eisner.urban_search.data.model.UrbanDefinition

sealed class SearchResult {
    object Error : SearchResult()
    object Loading : SearchResult()
    data class Data(val data: List<UrbanDefinition>) : SearchResult()
}