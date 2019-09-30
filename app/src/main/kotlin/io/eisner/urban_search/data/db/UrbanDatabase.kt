package io.eisner.urban_search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.eisner.urban_search.data.model.UrbanDefinition

@Database(entities = [UrbanDefinition::class], version = 1)
abstract class UrbanDatabase : RoomDatabase() {
    abstract fun urbanDefinitionDao(): UrbanDefinitionDao
}