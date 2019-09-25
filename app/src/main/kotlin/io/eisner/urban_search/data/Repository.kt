package io.eisner.urban_search.data

import io.eisner.urban_search.data.api.UrbanSearchApi
import io.eisner.urban_search.data.db.UrbanDatabase
import io.eisner.urban_search.data.model.UrbanDefinition
import io.reactivex.Flowable

class Repository(val database: UrbanDatabase, val api: UrbanSearchApi) {
    fun searchFor(word: String) : Flowable<UrbanDefinition> {
        return Flowable.empty()
    }
}