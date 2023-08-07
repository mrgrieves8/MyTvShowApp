package com.example.tvrawr.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tvrawr.data.models.TvShow

@Database(entities = [TvShow::class], version = 6, exportSchema = false)
abstract class TvShowDatabase : RoomDatabase() {

    abstract fun tvShowDao(): TvShowDao

    companion object {
        @Volatile
        private var INSTANCE: TvShowDatabase? = null

        fun getDatabase(context: Context): TvShowDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TvShowDatabase::class.java,
                    "tv_show_database"
                )
                    .fallbackToDestructiveMigration() // Enable destructive migrations
                    .build().also { INSTANCE = it }
            }
        }
    }
}
