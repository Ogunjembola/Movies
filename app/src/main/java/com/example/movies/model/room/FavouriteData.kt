package com.example.movies.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favourite")
data class FavouriteData(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movie_id: Int?,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "original_title")
    val original_title: String,
    @ColumnInfo(name = "release_date")
    val release_date: String,
    @ColumnInfo(name = "backdrop_path")
    val backdrop_path: String,
    @ColumnInfo(name = "poster_path")
    val poster_path: String,
    @ColumnInfo(name = "vote_average")
    val vote_average: Double?
    ) : Serializable