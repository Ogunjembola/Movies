package com.example.movies.view.paging

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.movies.model.remote.MovieApi
import com.example.movies.model.remote.Movies
import com.example.movies.utillz.NETWORK_PAGE_SIZE
import com.example.movies.view.MovieApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRespositry @Inject constructor(private val service: MovieApi){

    fun getNewMovies(): LiveData<PagingData<Movies>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                NewMoviesDataSource (service)
            }
        ).liveData
    }
}
