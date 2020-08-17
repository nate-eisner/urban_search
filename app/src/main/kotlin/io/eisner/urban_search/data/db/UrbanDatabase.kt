package io.eisner.urban_search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.eisner.urban_search.data.model.Track
import io.eisner.urban_search.data.model.TrackImage

@Database(entities = [Track::class, TrackImage::class], version = 2)
abstract class UrbanDatabase : RoomDatabase() {
    abstract fun urbanDefinitionDao(): UrbanDefinitionDao
}