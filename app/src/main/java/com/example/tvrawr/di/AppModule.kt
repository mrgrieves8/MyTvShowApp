package com.example.tvrawr.di

import android.app.Application
import android.content.Context
import com.example.tvrawr.data.local_db.TvShowDao
import com.example.tvrawr.data.local_db.TvShowDatabase
import com.example.tvrawr.data.remote_db.TvShowRemoteDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/") // TODO: Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideTvShowService(retrofit: Retrofit): TvShowRemoteDataSource {
        return retrofit.create(TvShowRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideTvShowDatabase(context: Context): TvShowDatabase {
        return TvShowDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTvShowDao(database: TvShowDatabase): TvShowDao {
        return database.tvShowDao()
    }


    @Provides
    @Singleton
    fun provideTvShowRemoteDataSource(retrofit: Retrofit): TvShowRemoteDataSource {
        return retrofit.create(TvShowRemoteDataSource::class.java)
    }

}
