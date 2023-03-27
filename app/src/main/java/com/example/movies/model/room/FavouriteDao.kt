package com.example.movies.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavouriteDao {
    @Insert
    suspend fun insert(favouriteData: FavouriteData)

    @Query("SELECT * FROM favourite")
    fun getAllMoviesData(): List<FavouriteData>

    @Query("SELECT * FROM favourite WHERE movie_id = :eId")
    fun getMoviesData(eId: Int): FavouriteData

    @Query("DELETE FROM favourite WHERE movie_id = :id")
    fun deleteMoviesData(id : Int)
}