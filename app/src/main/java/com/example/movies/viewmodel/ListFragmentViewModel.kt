package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movies.model.room.FavouriteDao
import com.example.movies.model.room.FavouriteData
import com.example.movies.model.remote.Movies
import com.example.movies.view.paging.MovieRespositry
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListFragmentViewModel @Inject constructor(
    private val respositry: MovieRespositry,
    application: Application,
    val favouriteDao: FavouriteDao
) : BaseViewModel(application) {


    val movieListLiveData = MutableLiveData<List<FavouriteData>>()
    //private val movieService = MovieApiServices
    private val disposable = CompositeDisposable()
    val movies = MutableLiveData<List<Movies>>()

    val movieLoadingError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    private var currentNewMovieLiveData: LiveData<PagingData<Movies>>? = null
   /* fun refresh() {
        //fetchRemoteData()
        fetchNewMovies()

    }*/
    fun fetchNewMovies(): LiveData<PagingData<Movies>> {
        val newResultLiveData: LiveData<PagingData<Movies>> = respositry.getNewMovies().cachedIn(viewModelScope)
        currentNewMovieLiveData = newResultLiveData
        return newResultLiveData
    }


   /* private suspend fun fetchRemoteData() {
        loading.value = true

        disposable.add(
            movieApi.getMovies(MY_API_KEY,1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieResult>() {
                    override fun onSuccess(result: MovieResult) {
                        movies.value = result.results
                        movieLoadingError.value = false
                        loading.value = false

                    }

                    override fun onError(e: Throwable) {
                        movieLoadingError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }*/
    fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            val movieData: List<FavouriteData> = favouriteDao.getAllMoviesData()
            movieListLiveData.postValue(movieData)
        }
    }
}