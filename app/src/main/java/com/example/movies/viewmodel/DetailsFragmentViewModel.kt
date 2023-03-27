package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movies.model.room.FavouriteDao
import com.example.movies.model.room.FavouriteData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(application: Application,val favouriteDao: FavouriteDao) : BaseViewModel(application) {

    val movieListLiveData = MutableLiveData<FavouriteData>()
    private val disposable = CompositeDisposable()
    val movieData = MutableLiveData<List<FavouriteData>>()
    val movieDataLoadingError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromDatabase()
    }

    private fun fetchFromDatabase() {
        viewModelScope.launch {
            loading.value = true
        }
    }

    private fun movieDataRetrieved(movie: FavouriteData) {
        movieData.value = listOf(movie)
        movieDataLoadingError.value = false
        loading.value = false
    }

    fun storeLocally(list: FavouriteData) {
        viewModelScope.launch {
            performAction(list)
            movieDataRetrieved(list)
        }
    }

    private suspend fun performAction(list: FavouriteData) {
        withContext(Dispatchers.IO) {
            val dao: FavouriteDao = favouriteDao
            dao.insert(list)
        }

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun fetch(uuid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movieData: FavouriteData =
                favouriteDao.getMoviesData(uuid)
            movieListLiveData.postValue(movieData)
        }
    }

    fun deleteMovieData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            favouriteDao.deleteMoviesData(id)

        }
    }
}