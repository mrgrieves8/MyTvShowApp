package com.example.tvrawr.ui

import androidx.lifecycle.*
import com.example.tvrawr.data.models.TvShow
import com.example.tvrawr.data.repository.TvShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(private val repository: TvShowRepository) : ViewModel() {
    val allFavoriteTvShows = repository.getFavoriteTvShows()
    var error = MutableLiveData<String>()

    // LiveData for search results and selected tvShow
    var searchResults = MutableLiveData<List<TvShow>>()
    var selectedTvShow = MutableLiveData<TvShow>()

    fun insert(tvShow: TvShow) = viewModelScope.launch {
        repository.insert(tvShow)
    }

    fun delete(tvShow: TvShow) {
        viewModelScope.launch {
            repository.delete(tvShow)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun deleteNonFavorites() = viewModelScope.launch {
        repository.deleteNonFavorites()
    }

    fun removeFromFavorites(tvShowId: Int) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(tvShowId, false)
        }
    }

    fun addtoFavorites(tvShowId: Int) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(tvShowId, true)
        }
    }

    fun isFavorite(tvShowId: Int): LiveData<Boolean?> {
        return repository.isFavorite(tvShowId)
    }


    fun getTvShow(id: Int): LiveData<TvShow>? {
        return repository.getTvShow(id)
    }
    suspend fun getTvShowSuspended(id: String): TvShow {
        return repository.getTvShowSuspended(id)
    }

    suspend fun doesTvShowExist(id: String): Boolean {
        return repository.doesTvShowExist(id)
    }

}
