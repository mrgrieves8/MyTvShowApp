package com.example.tvrawr.data.repository


import androidx.lifecycle.LiveData
import com.example.tvrawr.data.local_db.TvShowDao
import com.example.tvrawr.data.models.TvShow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepository @Inject constructor(
    private val tvShowDao: TvShowDao
) {
    fun getShowById(showId: Int): LiveData<TvShow?> {
        return tvShowDao.getShowById(showId)
    }
    fun getTvShows(): LiveData<List<TvShow>>? {
        return tvShowDao.getTvShows()
    }

    fun getTvShow(id: Int): LiveData<TvShow>? {
        return tvShowDao.getTvShow(id)
    }
    suspend fun getTvShowSuspended(id: String): TvShow {
        return tvShowDao.getTvShowSuspended(id)
    }

    suspend fun doesTvShowExist(id: String): Boolean {
        return tvShowDao.doesTvShowExist(id)
    }

    suspend fun insert(tvShow: TvShow): TvShow {
        val id = tvShowDao.insert(tvShow)
        tvShow.id = id.toInt()
        return tvShow
    }

    suspend fun delete(tvShow: TvShow) {
        tvShowDao.deleteTvShow(tvShow)
    }

    suspend fun update(tvShow: TvShow) {
        tvShowDao.update(tvShow)
    }

    suspend fun deleteAll() {
        tvShowDao.deleteAll()
    }

    suspend fun updateIsFavorite(tvShow: TvShow, isFavorite: Boolean) {
        tvShow.isFavorite = isFavorite
        update(tvShow)
    }

    suspend fun deleteNonFavorites() {
        tvShowDao.deleteNonFavorites()
    }
    suspend fun updateFavoriteStatus(tvShowId: Int, isFavorite: Boolean) {
        tvShowDao.updateFavoriteStatus(tvShowId, isFavorite)
    }

    fun getFavoriteTvShows(): LiveData<List<TvShow>> {
        return tvShowDao.getFavoriteTvShows()
    }


    fun isFavorite(showId: Int): LiveData<Boolean?> {
        return tvShowDao.isFavorite(showId)
    }

}
