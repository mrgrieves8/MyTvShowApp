package com.example.tvrawr.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tvshow_table")
data class TvShow(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "IMDbId") val IMDbId: String,
    @ColumnInfo(name = "genre") var genre: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "synopsis") var synopsis: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @PrimaryKey(autoGenerate = true) var id: Int? = null
)
