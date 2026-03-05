package com.example.tweetviewer.di

import com.example.tweetviewer.api.TweetApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This tells Hilt these objects live as long as the App
object NetworkModule {

    @Provides
    @Singleton // We only want exactly ONE instance of Retrofit in the entire app
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            // Using the base URL as discussed in Chapter 2
            .baseUrl("https://api.jsonbin.io/")
            // Teaching Retrofit how to parse JSON into our Tweet models
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTweetApiService(retrofit: Retrofit): TweetApiService {
        // Hilt is smart enough to see 'retrofit' as a parameter here and automatically
        // grab the Retrofit object we just built in the function above!
        return retrofit.create(TweetApiService::class.java)
    }
}