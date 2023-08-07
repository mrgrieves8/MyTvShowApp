package com.example.tvrawr.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tvrawr.data.models.TvShow

@Dao
interface TvShowDao {

    @Query("SELECT * FROM tvshow_table")
    fun getTvShows(): LiveData<List<TvShow>>
    @Query("SELECT * FROM tvshow_table WHERE id = :showId LIMIT 1")
    fun getShowById(showId: Int): LiveData<TvShow?>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tvShow: TvShow): Long

    @Update
    fun update(tvShow: TvShow)

    @Delete
    fun deleteTvShow(tvShow: TvShow)

    @Query("DELETE FROM tvshow_table")
    fun deleteAll()

    @Query("DELETE FROM tvshow_table WHERE isFavorite = 0")
    suspend fun deleteNonFavorites()

    @Query("SELECT * FROM tvshow_table WHERE id = :id")
    fun getTvShow(id: Int): LiveData<TvShow>

    @Query("SELECT * FROM tvshow_table WHERE IMDbId = :id")
    suspend fun getTvShowSuspended(id: String): TvShow

    @Query("SELECT EXISTS (SELECT 1 FROM tvshow_table WHERE IMDbId = :id)")
    suspend fun doesTvShowExist(id: String): Boolean

    @Query("SELECT * FROM tvshow_table WHERE isFavorite = 1")
    fun getFavoriteTvShows(): LiveData<List<TvShow>>

    @Query("UPDATE tvshow_table SET isFavorite = :isFavorite WHERE id = :tvShowId")
    suspend fun updateFavoriteStatus(tvShowId: Int, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM tvshow_table WHERE id = :showId LIMIT 1")
    fun isFavorite(showId: Int): LiveData<Boolean?>

}
