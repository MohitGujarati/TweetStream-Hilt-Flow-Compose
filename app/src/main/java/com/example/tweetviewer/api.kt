package com.example.tweetviewer.api

import com.example.tweetviewer.models.TweetsResponse
import retrofit2.Response
import retrofit2.http.GET

interface TweetApiService {

    // CORRECT: We only use the relative endpoint path here.
    // The "suspend" keyword allows this to be called seamlessly from a Coroutine.
    // Wrapping it in "Response" allows us to safely handle HTTP error codes later.
    @GET("v3/b/69992e37d0ea881f40cb75c1")
    suspend fun getTweets(): Response<TweetsResponse>

}