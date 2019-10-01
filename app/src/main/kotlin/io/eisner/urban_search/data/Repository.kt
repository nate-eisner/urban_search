package io.eisner.urban_search.data

import android.util.Log
import io.eisner.urban_search.data.api.UrbanSearchApi
import io.eisner.urban_search.data.db.UrbanDatabase
import io.eisner.urban_search.data.model.UrbanDefinition
import io.eisner.urban_search.testing.OpenForTesting
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Scheduler

enum class Sort {
    ThumbsUp,
    ThumbsDown,
}

@OpenForTesting
class Repository(
    private val database: UrbanDatabase,
    private val api: UrbanSearchApi,
    private val ioScheduler: Scheduler,
    private val computationScheduler: Scheduler
) {
    fun searchFor(word: String, sort: Sort): Flowable<List<UrbanDefinition>> {
        return Maybe.concat(
            listOf(
                database.urbanDefinitionDao().getDefinition(word)
                    .subscribeOn(ioScheduler)
                    .sort(sort)
                    .observeOn(computationScheduler),
                api.searchFor(word).toMaybe()
                    .map { response -> response.list }
                    .subscribeOn(ioScheduler)
                    .sort(sort)
                    .observeOn(computationScheduler)
                    .map { apiList ->
                        Log.d("UrbanSearch", "api list")
                        // save network results to DB
                        database.urbanDefinitionDao().insertDefinitions(apiList)
                        apiList
                    }.onErrorComplete()
            )
        )
    }

    private fun Maybe<List<UrbanDefinition>>.sort(sort: Sort): Maybe<List<UrbanDefinition>> {
        return map { definitions ->
            definitions.sortedBy { definition ->
                if (sort == Sort.ThumbsUp) {
                    definition.thumbsUp
                } else {
                    definition.thumbsDown
                }
            }.reversed() // puts highest at the top of the list
        }
    }
}