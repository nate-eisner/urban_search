package io.eisner.urban_search.data.db

import androidx.room.Query
import io.eisner.urban_search.data.model.UrbanDefinition
import io.reactivex.Flowable

interface UrbanDefinitionDao {
    @Query("SELECT * FROM urban_definitions WHERE word = :word")
    fun getDefinition(word: String) : Flowable<UrbanDefinition>

    @Query("DELETE FROM urban_definitions")
    fun clear()
}