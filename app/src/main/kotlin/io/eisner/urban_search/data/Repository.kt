package io.eisner.urban_search.data

import io.eisner.urban_search.data.api.UrbanSearchApi
import io.eisner.urban_search.data.db.UrbanDatabase
import io.eisner.urban_search.data.model.Track
import io.eisner.urban_search.testing.OpenForTesting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

enum class Sort {
    ASC,
    DSC,
}

enum class Source {
    Local,
    Remote
}

@OpenForTesting
class Repository(
    private val database: UrbanDatabase,
    private val api: UrbanSearchApi
) {
    suspend fun searchFor(name: String, sort: Sort): Flow<Pair<Source, List<Track>>> = flow {
        emit(Source.Local to database.urbanDefinitionDao().getTracks(name).sort(sort))
        val search = api.searchFor(name).results.trackMatches.track.sort(sort)
        emit(Source.Remote to search)
        database.urbanDefinitionDao().insertDefinitions(search)
    }

    private fun List<Track>.sort(sort: Sort): List<Track> {
        val sortedBy = this.sortedBy { it.listeners }
        return when (sort) {
            Sort.ASC -> sortedBy
            Sort.DSC -> sortedBy.reversed()
        }
    }
}