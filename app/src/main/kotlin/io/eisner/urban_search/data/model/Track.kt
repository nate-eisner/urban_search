package io.eisner.urban_search.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "tracks", primaryKeys = ["name", "artist"])
@JsonClass(generateAdapter = true)
data class Track(
    val name: String,
    val artist: String,
    val listeners: Int,
    val url: String,
    @Ignore val image: List<TrackImage>
) {
    constructor(
        name: String,
        artist: String,
        listeners: Int,
        url: String
    ) : this(name, artist, listeners, url, emptyList())
}

@Entity
@JsonClass(generateAdapter = true)
data class TrackImage(@PrimaryKey @Json(name = "#text") val url: String, val size: String)