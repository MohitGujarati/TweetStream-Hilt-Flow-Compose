package com.example.tweetviewer.repository

import com.example.tweetviewer.api.TweetApiService
import com.example.tweetviewer.models.Tweet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// 2. The Implementation: We use @Inject constructor so Hilt automatically provides the ApiService
class TweetRepositoryImpl @Inject constructor(
    private val apiService: TweetApiService
) : TweetRepository {

    override fun getTweets(): Flow<Result<List<Tweet>>> = flow {
        try {
            // Make the network call
            val response = apiService.getTweets()

            // CORRECT SOLUTION: Check for HTTP Success (Codes 200-299)
            if (response.isSuccessful) {
                // Safely extract the tweets. If anything is null, default to an empty list.
                val tweets = response.body()?.record?.tweets ?: emptyList()
                emit(Result.success(tweets))
            } else {
                // Handled HTTP errors (like 404 or 500)
                emit(Result.failure(Exception("Server Error: ${response.code()}")))
            }

        } catch (e: Exception) {
            // Handles actual network crashes (No internet, timeout, etc.)
            emit(Result.failure(e))
        }
    }
}