package com.example.movies.model.di

import android.app.Application
import androidx.room.Room
import com.example.movies.model.room.FavouriteDao
import com.example.movies.model.data.MovieDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        application: Application,
        callback: MovieDataBase.Callback
    )
            : MovieDataBase {
        return Room.databaseBuilder(
            application,
            MovieDataBase::class.java,
            "maoviedatabase"
        )
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideMovieDataBase(
        db:

        MovieDataBase
    )
            : FavouriteDao {
        return db.getFavouriteDao()
    }
}