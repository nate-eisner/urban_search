package io.eisner.urban_search.data

import io.eisner.urban_search.data.api.UrbanSearchApi
import io.eisner.urban_search.data.db.UrbanDatabase
import io.eisner.urban_search.data.model.UrbanDefinition
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler

enum class Sort {
    ThumbsUp,
    ThumbsDown,
}

class Repository(
    private val database: UrbanDatabase,
    private val api: UrbanSearchApi,
    private val ioScheduler: Scheduler,
    private val computationScheduler: Scheduler
) {
    fun searchFor(word: String, sort: Sort): Flowable<List<UrbanDefinition>> {
        return Flowable.create({ emitter ->
            val localDbRead =
                database.urbanDefinitionDao().getDefinition(word)
                    .subscribeOn(ioScheduler)
                    .sort(sort)
                    .observeOn(computationScheduler)
                    .subscribe { emitter.onNext(it) }
            val networkSearch = api.searchFor(word)
                .map { response -> response.list }
                .subscribeOn(ioScheduler)
                .sort(sort)
                .observeOn(computationScheduler)
                .map {
                    // save network results to DB
                    database.urbanDefinitionDao().insertDefinitions(it)
                    it
                }
                .subscribe { emitter.onNext(it) }
            emitter.setCancellable {
                localDbRead.dispose()
                networkSearch.dispose()
            }
        }, BackpressureStrategy.LATEST)
    }

    private fun Observable<List<UrbanDefinition>>.sort(sort: Sort): Observable<List<UrbanDefinition>> {
        return map { definitions ->
            definitions.sortedBy { definition ->
                if (sort == Sort.ThumbsUp) {
                    definition.thumbsUp
                } else {
                    definition.thumbsDown
                }
            }
        }
    }
}