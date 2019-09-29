package io.eisner.urban_search.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.eisner.urban_search.data.model.UrbanDefinition
import io.reactivex.Observable

@Dao
interface UrbanDefinitionDao {
    @Query("SELECT * FROM urban_definitions WHERE word = :word")
    fun getDefinition(word: String): Observable<List<UrbanDefinition>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDefinitions(definitions: List<UrbanDefinition>)

    @Query("DELETE FROM urban_definitions")
    fun clear()
}