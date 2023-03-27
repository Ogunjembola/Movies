package com.example.movies.model.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movies.model.room.FavouriteDao
import com.example.movies.model.room.FavouriteData
import com.example.movies.model.di.AppModule
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [FavouriteData::class], version = 2)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun getFavouriteDao(): FavouriteDao

    class Callback @Inject constructor(
        private val database: Provider<MovieDataBase>,
        @AppModule.ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}