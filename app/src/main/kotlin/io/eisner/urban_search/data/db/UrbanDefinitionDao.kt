package io.eisner.urban_search.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.eisner.urban_search.data.model.Track

@Dao
interface UrbanDefinitionDao {
    @Query("SELECT * FROM tracks WHERE name = :songName")
    suspend fun getTracks(songName: String): List<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDefinitions(definitions: List<Track>)

    @Query("DELETE FROM tracks")
    fun clear()
}