package com.example.movies.view.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.model.remote.MovieApi
import com.example.movies.model.remote.Movies
import com.example.movies.utillz.MY_API_KEY
import com.example.movies.utillz.STARTING_PAGE_INDEX

class NewMoviesDataSource (private val service: MovieApi
) : PagingSource<Int, Movies>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        val page = params.key ?:STARTING_PAGE_INDEX
        return try {

            val response = service.getMovies(MY_API_KEY,page)
            val document = response.body()?.results
            val responseData = mutableListOf<Movies>()
            if (document != null) {
                responseData.addAll(document)
            }

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = page + 1
            )

        }  catch (exception: Exception) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition
    }
}