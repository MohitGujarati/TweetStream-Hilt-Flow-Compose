package com.example.tweetviewer.di

import com.example.tweetviewer.repository.TweetRepository
import com.example.tweetviewer.repository.TweetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // 3. The Binding: This tells Hilt, "Whenever a class asks for a 'TweetRepository',
    // give them the 'TweetRepositoryImpl' that you know how to build."
    @Binds
    abstract fun bindTweetRepository(
        tweetRepositoryImpl: TweetRepositoryImpl
    ): TweetRepository
}