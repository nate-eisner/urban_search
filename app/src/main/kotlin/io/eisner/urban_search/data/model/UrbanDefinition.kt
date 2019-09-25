package io.eisner.urban_search.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "urban_definitions")
@JsonClass(generateAdapter = true)
data class UrbanDefinition(
    @Json(name = "defid") @PrimaryKey val id: Long, val definition: String, val word: String,
    @Json(name = "thumbs_up") var thumbsUp: Int, @Json(name = "thumbs_down") var thumbsDown: Int
)