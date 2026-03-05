package com.example.tweetviewer.repository

import com.example.tweetviewer.models.Tweet
import kotlinx.coroutines.flow.Flow

interface TweetRepository {
    fun getTweets(): Flow<Result<List<Tweet>>>
}